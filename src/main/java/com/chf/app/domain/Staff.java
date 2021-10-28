package com.chf.app.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chf.app.domain.id.UserId;

@Entity
@Table(name = "staff")
public class Staff {

    @EmbeddedId
    private UserId id;

    @Column(name = "staff_id", length = 15, unique = true)
    private String staffId; // 工号

    public Staff() {
    }

    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

}
