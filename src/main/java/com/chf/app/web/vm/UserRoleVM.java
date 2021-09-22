package com.chf.app.web.vm;

import java.util.List;

import javax.validation.constraints.NotNull;

public class UserRoleVM {

    @NotNull
    private String login;

    private List<String> roleIds;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

}
