package com.chf.app.web.vm;

import java.util.List;

public class NavigationRoleVM {

    private String roleId;

    private List<String> navigationId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getNavigationId() {
        return navigationId;
    }

    public void setNavigationId(List<String> navigationId) {
        this.navigationId = navigationId;
    }

}
