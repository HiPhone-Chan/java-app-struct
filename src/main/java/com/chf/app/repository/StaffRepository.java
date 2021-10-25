package com.chf.app.repository;

import com.chf.app.domain.Staff;
import com.chf.app.domain.id.UserId;
import com.chf.app.repository.support.JpaExtRepository;

public interface StaffRepository extends JpaExtRepository<Staff, UserId> {

}
