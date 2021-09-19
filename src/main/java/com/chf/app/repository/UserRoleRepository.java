package com.chf.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.User;
import com.chf.app.domain.UserRole;
import com.chf.app.domain.id.UserRoleId;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    List<UserRole> findAllByIdUser(User user);

    Page<UserRole> findByIdUser(Pageable pageable, User user);

    void deleteByIdUser(User user);
}
