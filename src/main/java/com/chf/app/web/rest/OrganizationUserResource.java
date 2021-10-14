package com.chf.app.web.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.domain.Organization;
import com.chf.app.domain.OrganizationUser;
import com.chf.app.domain.User;
import com.chf.app.domain.id.OrganizationUserId;
import com.chf.app.repository.OrganizationRepository;
import com.chf.app.repository.OrganizationUserRepository;
import com.chf.app.repository.UserRepository;
import com.chf.app.service.OrganizationService;
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

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/organization-user")
    public void createOrganizationUser(@Valid @RequestBody OrganizationUserVM organizationUserVM) {
        Organization org = organizationRepository.findById(organizationUserVM.getOrganizationId()).orElseThrow();

        userRepository.findOneByLogin(organizationUserVM.getLogin()).ifPresent(user -> {
            OrganizationUser organizationUser = new OrganizationUser();
            organizationUser.setId(new OrganizationUserId(org, user));
            organizationUserRepository.save(organizationUser);
        });
    }

    @DeleteMapping("/organization-user")
    public void deleteOrganizationUser(@RequestParam String organizationId, @RequestParam String login) {
        Organization org = organizationRepository.findById(organizationId).orElseThrow();
        User user = userRepository.findOneByLogin(login).orElseThrow();

        organizationUserRepository.deleteById(new OrganizationUserId(org, user));
    }

    @GetMapping("/organization-users")
    public ResponseEntity<List<AdminUserDTO>> getOrganizationUsers(Pageable pageable, String organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow();
        return ResponseUtil.wrapPage(organizationService.getOrganizationUsers(pageable, organization));
    }
}
