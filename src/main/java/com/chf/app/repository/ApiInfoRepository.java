package com.chf.app.repository;

import java.util.Optional;

import com.chf.app.domain.ApiInfo;
import com.chf.app.repository.support.JpaExtRepository;

public interface ApiInfoRepository extends JpaExtRepository<ApiInfo, String> {

    Optional<ApiInfo> findOneByMethodAndPath(String method, String path);

    boolean existsByMethodAndPath(String method, String path);

}
