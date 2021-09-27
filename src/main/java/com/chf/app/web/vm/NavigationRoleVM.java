package com.chf.app.web.vm;

import java.util.List;

public class NavigationRoleVM {

    private String roleId;

    private List<String> navigationIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getNavigationIds() {
        return navigationIds;
    }

    public void setNavigationIds(List<String> navigationIds) {
        this.navigationIds = navigationIds;
    }

}
