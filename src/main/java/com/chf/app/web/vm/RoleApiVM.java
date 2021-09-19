package com.chf.app.web.vm;

import java.util.List;

public class RoleApiVM {

    private String roleId;

    private List<String> apiId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getApiId() {
        return apiId;
    }

    public void setApiId(List<String> apiId) {
        this.apiId = apiId;
    }

}
