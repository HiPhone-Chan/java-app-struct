package com.chf.app.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chf.app.domain.id.NavigationRoleId;

// 组织权限关系
@Entity
@Table(name = "navigation_role")
public class NavigationRole {

    @EmbeddedId
    private NavigationRoleId id;

    public NavigationRoleId getId() {
        return id;
    }

    public void setId(NavigationRoleId id) {
        this.id = id;
    }

}
