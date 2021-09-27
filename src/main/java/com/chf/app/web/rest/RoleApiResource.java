package com.chf.app.web.rest;

import java.util.ArrayList;
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
import com.chf.app.domain.RoleApi;
import com.chf.app.domain.StaffRole;
import com.chf.app.domain.id.RoleApiId;
import com.chf.app.exception.ServiceException;
import com.chf.app.repository.ApiInfoRepository;
import com.chf.app.repository.RoleApiRepository;
import com.chf.app.repository.StaffRoleRepository;
import com.chf.app.web.vm.RoleApiVM;

@RestController
@RequestMapping("/api/manager")
@Transactional
public class RoleApiResource {

    @Autowired
    private RoleApiRepository roleApiRepository;

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private StaffRoleRepository roleRepository;

    @PutMapping("/role-api")
    public void saveRoleApi(@RequestBody RoleApiVM roleApiVM) {
        StaffRole role = roleRepository.findById(roleApiVM.getRoleId())
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));

        List<RoleApi> list = new ArrayList<>();
        for (String apiId : roleApiVM.getApiIds()) {
            apiInfoRepository.findById(apiId).ifPresent(apiInfo -> {
                RoleApi roleApi = new RoleApi();
                roleApi.setId(new RoleApiId(role, apiInfo));
                list.add(roleApi);
            });
        }
        roleApiRepository.saveAll(list);
    }

    @GetMapping("/role-api/apis")
    public List<ApiInfo> getRoleApis(@RequestParam String  roleId) {
        StaffRole role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));

        List<ApiInfo> page = roleApiRepository.findAllByIdRole(role).stream().map(roleApi -> {
            return roleApi.getId().getApiInfo();
        }).collect(Collectors.toList());
        return page;
    }

}
