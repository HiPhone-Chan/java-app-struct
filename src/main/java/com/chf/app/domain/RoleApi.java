package com.chf.app.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chf.app.domain.id.RoleApiId;

//导航使用到的api关系
@Entity
@Table(name = "role_api")
public class RoleApi {

    @EmbeddedId
    private RoleApiId id;

    public RoleApiId getId() {
        return id;
    }

    public void setId(RoleApiId id) {
        this.id = id;
    }

}
