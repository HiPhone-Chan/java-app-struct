package com.chf.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.chf.app.domain.Organization;
import com.chf.app.repository.OrganizationUserRepository;
import com.chf.app.service.dto.AdminUserDTO;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationUserRepository organizationUserRepository;

    public Page<AdminUserDTO> getOrganizationUsers(Pageable pageable, Organization organization) {
        return organizationUserRepository.findByIdOrganization(pageable, organization).map(orgUser -> {
            return new AdminUserDTO(orgUser.getId().getUser());
        });
    }

}
