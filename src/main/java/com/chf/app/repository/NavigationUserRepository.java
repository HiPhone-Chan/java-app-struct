package com.chf.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.NavigationUser;
import com.chf.app.domain.id.NavigationUserId;

public interface NavigationUserRepository extends JpaRepository<NavigationUser, NavigationUserId> {

}
