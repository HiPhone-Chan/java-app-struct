package com.chf.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
