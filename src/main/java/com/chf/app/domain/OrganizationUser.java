package com.chf.app.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chf.app.domain.id.OrganizationUserId;

// 组织用户关系
@Entity
@Table(name = "organization_user")
public class OrganizationUser {

    @EmbeddedId
    private OrganizationUserId id;

    public OrganizationUserId getId() {
        return id;
    }

    public void setId(OrganizationUserId id) {
        this.id = id;
    }

}
