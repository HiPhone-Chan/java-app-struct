package com.chf.app.service.dto;

import com.chf.app.domain.User;

public class AdminStaffDTO extends AdminUserDTO {

    private String staffId;

    public AdminStaffDTO() {

    }

    public AdminStaffDTO(User user) {
        super(user);
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

}
