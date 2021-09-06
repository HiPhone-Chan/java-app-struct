package com.chf.app.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chf.app.domain.id.NavigationUserId;

// 组织用户关系
@Entity
@Table(name = "organization_user")
public class NavigationUser {

    @EmbeddedId
    private NavigationUserId id;

    public NavigationUserId getId() {
        return id;
    }

    public void setId(NavigationUserId id) {
        this.id = id;
    }

}
