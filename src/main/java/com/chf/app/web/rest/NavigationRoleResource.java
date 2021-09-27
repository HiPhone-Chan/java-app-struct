package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.domain.NavigationRole;
import com.chf.app.domain.StaffRole;
import com.chf.app.domain.id.NavigationRoleId;
import com.chf.app.exception.ServiceException;
import com.chf.app.repository.NavigationRepository;
import com.chf.app.repository.NavigationRoleRepository;
import com.chf.app.repository.StaffRoleRepository;
import com.chf.app.web.util.ResponseUtil;
import com.chf.app.web.vm.NavigationRoleVM;
import com.chf.app.web.vm.NavigationVM;

@RestController
@RequestMapping("/api/manager")
@Transactional
public class NavigationRoleResource {

    @Autowired
    private NavigationRoleRepository navigationRoleRepository;

    @Autowired
    private StaffRoleRepository staffRoleRepository;

    @Autowired
    private NavigationRepository navigationRepository;

    @PutMapping("/navigation-role")
    public void updateNavigationRole(@RequestBody NavigationRoleVM navigationRoleVM) {
        staffRoleRepository.findById(navigationRoleVM.getRoleId()).ifPresent(role -> {
            navigationRoleRepository.deleteByIdRole(role);

            List<NavigationRole> list = new ArrayList<>();
            for (String navId : navigationRoleVM.getNavigationIds()) {
                navigationRepository.findById(navId).ifPresent(navigation -> {
                    NavigationRole navigationRole = new NavigationRole();
                    navigationRole.setId(new NavigationRoleId(navigation, role));
                    list.add(navigationRole);
                });
            }
            navigationRoleRepository.saveAll(list);
        });
    }

    @GetMapping("/navigation-role/navigations")
    public ResponseEntity<List<NavigationVM>> getRoleNavigations(Pageable pageable, @RequestParam String roleId) {
        StaffRole role = staffRoleRepository.findById(roleId)
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));

        Page<NavigationVM> page = navigationRoleRepository.findByIdRole(pageable, role).map(navigationRole -> {
            return new NavigationVM(navigationRole.getId().getNavigation());
        });
        return ResponseUtil.wrapPage(page);
    }

}
