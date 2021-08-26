package com.chf.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chf.app.domain.OperationLog;

public interface OperationLogRepository extends JpaRepository<OperationLog, String> {

}
