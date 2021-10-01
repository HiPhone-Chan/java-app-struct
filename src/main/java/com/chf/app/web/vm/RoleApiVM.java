package com.chf.app.web.vm;

import java.util.List;

public class RoleApiVM {

    private String roleId;

    private List<String> apiIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getApiIds() {
        return apiIds;
    }

    public void setApiIds(List<String> apiIds) {
        this.apiIds = apiIds;
    }

}
