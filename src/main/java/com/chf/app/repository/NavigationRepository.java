package com.chf.app.repository;

import java.util.List;

import com.chf.app.domain.Navigation;
import com.chf.app.repository.support.JpaExtRepository;

public interface NavigationRepository extends JpaExtRepository<Navigation, String> {

    boolean existsByParent(Navigation parent);

    List<Navigation> findAllByParent(Navigation parent);

}
