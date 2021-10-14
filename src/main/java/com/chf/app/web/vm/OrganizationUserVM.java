package com.chf.app.web.vm;

import javax.validation.constraints.NotNull;

public class OrganizationUserVM {

    @NotNull
    private String organizationId;

    private String login;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
