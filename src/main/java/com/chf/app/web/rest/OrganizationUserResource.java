package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.domain.Organization;
import com.chf.app.domain.OrganizationUser;
import com.chf.app.domain.User;
import com.chf.app.domain.id.OrganizationUserId;
import com.chf.app.repository.OrganizationRepository;
import com.chf.app.repository.OrganizationUserRepository;
import com.chf.app.repository.UserRepository;
import com.chf.app.service.dto.AdminUserDTO;
import com.chf.app.web.util.ResponseUtil;
import com.chf.app.web.vm.OrganizationUserVM;

@RestController
@RequestMapping("/api/manager")
public class OrganizationUserResource {

    @Autowired
    private OrganizationUserRepository organizationUserRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/organization-user")
    public void saveOrganizationUser(@Valid @RequestBody OrganizationUserVM organizationUserVM) {
        Organization org = organizationRepository.findById(organizationUserVM.getOrganizationId()).orElseThrow();

        List<OrganizationUser> list = new ArrayList<>();
        for (String login : organizationUserVM.getLogins()) {

            userRepository.findOneByLogin(login).ifPresent(user -> {
                OrganizationUser organizationUser = new OrganizationUser();
                organizationUser.setId(new OrganizationUserId(org, user));
                list.add(organizationUser);
            });
        }
        organizationUserRepository.deleteByIdOrganization(org);
        organizationUserRepository.saveAll(list);
    }

    @GetMapping("/organization-users")
    public ResponseEntity<List<AdminUserDTO>> getOrganizationUsers(Pageable pageable, String login) {
        User user = userRepository.findOneByLogin(login).orElseThrow();
        Page<AdminUserDTO> page = organizationUserRepository.findByIdUser(pageable, user).map(orgUser -> {
            return new AdminUserDTO(orgUser.getId().getUser());
        });
        return ResponseUtil.wrapPage(page);
    }
}
