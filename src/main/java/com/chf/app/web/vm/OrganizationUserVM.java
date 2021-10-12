package com.chf.app.web.vm;

import java.util.List;

import javax.validation.constraints.NotNull;

public class OrganizationUserVM {

    @NotNull
    private String organizationId;

    private List<String> logins;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public List<String> getLogins() {
        return logins;
    }

    public void setLogins(List<String> logins) {
        this.logins = logins;
    }

}
