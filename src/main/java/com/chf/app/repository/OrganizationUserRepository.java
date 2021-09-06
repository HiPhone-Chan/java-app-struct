package com.chf.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.OrganizationUser;
import com.chf.app.domain.id.OrganizationUserId;

public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, OrganizationUserId> {

}
