package com.chf.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {

}
