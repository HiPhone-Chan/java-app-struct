package com.chf.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.Organization;
import com.chf.app.domain.OrganizationUser;
import com.chf.app.domain.User;
import com.chf.app.domain.id.OrganizationUserId;

public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, OrganizationUserId> {

    void deleteByIdOrganization(Organization organization);

    Page<OrganizationUser> findByIdUser(Pageable pageable, User user);

}
