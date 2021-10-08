package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.domain.ApiInfo;
import com.chf.app.domain.Navigation;
import com.chf.app.domain.NavigationApi;
import com.chf.app.domain.RoleApi;
import com.chf.app.domain.StaffRole;
import com.chf.app.domain.User;
import com.chf.app.domain.UserRole;
import com.chf.app.domain.id.NavigationApiId;
import com.chf.app.domain.id.RoleApiId;
import com.chf.app.domain.id.UserRoleId;
import com.chf.app.repository.ApiInfoRepository;
import com.chf.app.repository.NavigationApiRepository;
import com.chf.app.repository.NavigationRepository;
import com.chf.app.repository.RoleApiRepository;
import com.chf.app.repository.UserRoleRepository;
import com.chf.app.service.UserService;
import com.chf.app.web.vm.NavigationApiVM;

@RestController
@RequestMapping("/api")
@Transactional
public class NavigationApiResource {

    @Autowired
    private NavigationApiRepository navigationApiRepository;

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private NavigationRepository navigationRepository;

    @Autowired
    private RoleApiRepository roleApiRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/manager/navigation-api")
    public void createNavigationApi(@RequestBody NavigationApiVM navigationApiVM) {
        Navigation navigation = navigationRepository.findById(navigationApiVM.getNavId()).orElseThrow();
        ApiInfo apiInfo = apiInfoRepository.findById(navigationApiVM.getApiId()).orElseThrow();
        NavigationApi navigationApi = new NavigationApi();
        navigationApi.setId(new NavigationApiId(navigation, apiInfo));
        navigationApiRepository.save(navigationApi);
    }

    @GetMapping("/manager/navigation-api/apis")
    public List<ApiInfo> getNavigationApis(@RequestParam String navId) {
        Navigation navigation = navigationRepository.findById(navId).orElseThrow();

        List<ApiInfo> list = navigationApiRepository.findAllByIdNavigation(navigation).stream().map(navigationApi -> {
            return navigationApi.getId().getApiInfo();
        }).collect(Collectors.toList());
        return list;
    }

    // 获取员工菜单中的api
    @GetMapping("/staff/navigation-api/apis")
    public List<ApiInfo> getStaffNavigationApis(@RequestParam String navId) {
        User user = userService.getUserWithAuthorities().orElseThrow();
        List<StaffRole> userRoleList = userRoleRepository.findAllByIdUser(user).stream().map(UserRole::getId)
                .map(UserRoleId::getRole).collect(Collectors.toList());

        Navigation navigation = navigationRepository.findById(navId).orElseThrow();
        List<ApiInfo> apiInfoList = navigationApiRepository.findAllByIdNavigation(navigation).stream()
                .map(navigationApi -> {
                    return navigationApi.getId().getApiInfo();
                }).collect(Collectors.toList());

        List<ApiInfo> result = new ArrayList<>();
        for (ApiInfo apiInfo : apiInfoList) {
            List<StaffRole> apiRoleList = roleApiRepository.findAllByIdApiInfo(apiInfo).stream().map(RoleApi::getId)
                    .map(RoleApiId::getRole).collect(Collectors.toList());
            for (StaffRole userRole : userRoleList) {
                if (apiRoleList.contains(userRole)) {
                    result.add(apiInfo);
                    break;
                }
            }
        }
        return result;
    }

    @DeleteMapping("/manager/navigation-api")
    public void deleteNavigationApi(@RequestParam String navId, @RequestParam String apiId) {
        Navigation navigation = navigationRepository.findById(navId).orElseThrow();
        ApiInfo apiInfo = apiInfoRepository.findById(apiId).orElseThrow();
        NavigationApi navigationApi = new NavigationApi();
        navigationApi.setId(new NavigationApiId(navigation, apiInfo));
        navigationApiRepository.delete(navigationApi);
    }

}
