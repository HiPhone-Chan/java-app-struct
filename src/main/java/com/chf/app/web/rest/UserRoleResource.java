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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.domain.StaffRole;
import com.chf.app.domain.User;
import com.chf.app.domain.UserRole;
import com.chf.app.domain.id.UserRoleId;
import com.chf.app.exception.ServiceException;
import com.chf.app.repository.StaffRoleRepository;
import com.chf.app.repository.UserRoleRepository;
import com.chf.app.service.UserService;
import com.chf.app.web.util.ResponseUtil;
import com.chf.app.web.vm.UserRoleVM;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/staff")
@Transactional
public class UserRoleResource {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private StaffRoleRepository staffRoleRepository;

    @Autowired
    private UserService userService;

    @PutMapping("/user-role")
    public void updateUserRole(@RequestBody List<UserRoleVM> userRoleVMList) {
        userService.getUserWithAuthorities().ifPresent(user -> {
            userRoleRepository.deleteByIdUser(user);

            List<UserRole> list = new ArrayList<>();
            for (UserRoleVM userRoleVM : userRoleVMList) {
                staffRoleRepository.findById(userRoleVM.getRoleId()).ifPresent(role -> {
                    UserRole userRole = new UserRole();
                    userRole.setId(new UserRoleId(user, role));
                    list.add(userRole);
                });
            }
            userRoleRepository.saveAll(list);
        });
    }

    @GetMapping("/user-roles")
    public ResponseEntity<List<StaffRole>> getUserRoles(Pageable pageable) {
        User user = userService.getUserWithAuthorities()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));
        Page<StaffRole> page = userRoleRepository.findByIdUser(pageable, user).map(userRole -> {
            return userRole.getId().getRole();
        });
        return ResponseUtil.wrapPage(page);
    }

}
