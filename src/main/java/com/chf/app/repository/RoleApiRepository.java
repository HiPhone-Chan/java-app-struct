package com.chf.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.ApiInfo;
import com.chf.app.domain.RoleApi;
import com.chf.app.domain.StaffRole;
import com.chf.app.domain.id.RoleApiId;

public interface RoleApiRepository extends JpaRepository<RoleApi, RoleApiId> {

    List<RoleApi> findAllByIdRole(StaffRole role);

    List<RoleApi> findAllByIdApiInfo(ApiInfo apiInfo);

}
