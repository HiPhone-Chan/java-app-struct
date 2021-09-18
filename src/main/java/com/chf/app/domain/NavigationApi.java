package com.chf.app.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chf.app.domain.id.NavigationApiId;

//导航使用到的api关系
@Entity
@Table(name = "navigation_api")
public class NavigationApi {

    @EmbeddedId
    private NavigationApiId id;

    public NavigationApiId getId() {
        return id;
    }

    public void setId(NavigationApiId id) {
        this.id = id;
    }

}
