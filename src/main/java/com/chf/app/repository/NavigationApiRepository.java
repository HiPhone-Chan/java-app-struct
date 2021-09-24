package com.chf.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.Navigation;
import com.chf.app.domain.NavigationApi;
import com.chf.app.domain.id.NavigationApiId;

public interface NavigationApiRepository extends JpaRepository<NavigationApi, NavigationApiId> {

    boolean existsByIdNavigation(Navigation navigation);

    List<NavigationApi> findAllByIdNavigation(Navigation navigation);

}
