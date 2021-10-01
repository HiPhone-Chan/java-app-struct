package com.chf.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.NavigationRole;
import com.chf.app.domain.StaffRole;
import com.chf.app.domain.id.NavigationRoleId;

public interface NavigationRoleRepository extends JpaRepository<NavigationRole, NavigationRoleId> {

    void deleteByIdRole(StaffRole role);

    List<NavigationRole> findAllByIdRole(StaffRole role);

    Page<NavigationRole> findByIdRole(Pageable pageable, StaffRole role);

}
