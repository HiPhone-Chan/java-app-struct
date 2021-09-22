package com.chf.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chf.app.constants.AuthoritiesConstants;
import com.chf.app.domain.StaffRole;
import com.chf.app.repository.ApiInfoRepository;
import com.chf.app.repository.RoleApiRepository;
import com.chf.app.repository.UserRepository;
import com.chf.app.repository.UserRoleRepository;
import com.chf.app.security.SecurityUtils;

@Service
@Transactional
public class RoleService {

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleApiRepository roleApiRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean hasCurrentUserThisAuthority(String method, String path) {
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.MANAGER)) {
            return true;
        }

        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin).map(user -> {
            return apiInfoRepository.findOneByMethodAndPath(method, path).map(apiInfo -> {
                List<StaffRole> neededList = roleApiRepository.findAllByIdApiInfo(apiInfo).stream().map(roleApi -> {
                    return roleApi.getId().getRole();
                }).collect(Collectors.toList());
                List<StaffRole> hasList = userRoleRepository.findAllByIdUser(user).stream().map(roleApi -> {
                    return roleApi.getId().getRole();
                }).collect(Collectors.toList());

                for (StaffRole needed : neededList) {
                    if (hasList.contains(needed)) {
                        return true;
                    }
                }
                return true;
            }).orElse(false);
        }).orElse(false);

    }
}
