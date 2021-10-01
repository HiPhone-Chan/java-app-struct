package com.chf.app.web.rest;

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
import com.chf.app.domain.id.NavigationApiId;
import com.chf.app.repository.ApiInfoRepository;
import com.chf.app.repository.NavigationApiRepository;
import com.chf.app.repository.NavigationRepository;
import com.chf.app.web.vm.NavigationApiVM;

@RestController
@RequestMapping("/api/manager")
@Transactional
public class NavigationApiResource {

    @Autowired
    private NavigationApiRepository navigationApiRepository;

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private NavigationRepository navigationRepository;

    @PostMapping("/navigation-api")
    public void createNavigationApi(@RequestBody NavigationApiVM navigationApiVM) {
        Navigation navigation = navigationRepository.findById(navigationApiVM.getNavId()).orElseThrow();
        ApiInfo apiInfo = apiInfoRepository.findById(navigationApiVM.getApiId()).orElseThrow();
        NavigationApi navigationApi = new NavigationApi();
        navigationApi.setId(new NavigationApiId(navigation, apiInfo));
        navigationApiRepository.save(navigationApi);
    }

    @GetMapping("/navigation-api/apis")
    public List<ApiInfo> getNavigationApis(@RequestParam String navId) {
        Navigation navigation = navigationRepository.findById(navId).orElseThrow();

        List<ApiInfo> page = navigationApiRepository.findAllByIdNavigation(navigation).stream().map(navigationApi -> {
            return navigationApi.getId().getApiInfo();
        }).collect(Collectors.toList());
        return page;
    }

    @DeleteMapping("/navigation-api")
    public void deleteNavigationApi(@RequestParam String navId, @RequestParam String apiId) {
        Navigation navigation = navigationRepository.findById(navId).orElseThrow();
        ApiInfo apiInfo = apiInfoRepository.findById(apiId).orElseThrow();
        NavigationApi navigationApi = new NavigationApi();
        navigationApi.setId(new NavigationApiId(navigation, apiInfo));
        navigationApiRepository.delete(navigationApi);
    }

}
