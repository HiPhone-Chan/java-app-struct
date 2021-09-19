package com.chf.app.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.domain.ApiInfo;
import com.chf.app.domain.Navigation;
import com.chf.app.domain.NavigationApi;
import com.chf.app.domain.id.NavigationApiId;
import com.chf.app.exception.ServiceException;
import com.chf.app.repository.ApiInfoRepository;
import com.chf.app.repository.NavigationApiRepository;
import com.chf.app.repository.NavigationRepository;
import com.chf.app.web.vm.NavigationApiVM;

@RestController
@RequestMapping("/api/staff")
@Transactional
public class NavigationApiResource {

    @Autowired
    private NavigationApiRepository navigationApiRepository;

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private NavigationRepository navigationRepository;

    @PutMapping("/navigation-api")
    public void createNavigationApi(@RequestBody NavigationApiVM navigationApiVM) {
        Navigation navigation = navigationRepository.findById(navigationApiVM.getNavId())
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));
        ApiInfo apiInfo = apiInfoRepository.findById(navigationApiVM.getApiId())
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));
        NavigationApi navigationApi = new NavigationApi();
        navigationApi.setId(new NavigationApiId(navigation, apiInfo));
        navigationApiRepository.save(navigationApi);
    }

    @GetMapping("/navigation-api/apis")
    public List<ApiInfo> getNavigationApis(@RequestParam String navId) {
        Navigation navigation = navigationRepository.findById(navId)
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));

        List<ApiInfo> page = navigationApiRepository.findAllByIdNavigation(navigation).stream().map(navigationApi -> {
            return navigationApi.getId().getApiInfo();
        }).collect(Collectors.toList());
        return page;
    }

}
