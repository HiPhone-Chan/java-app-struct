package com.chf.app.web.vm;

import com.chf.app.domain.Navigation;

public class NavigationVM {

    private String id;

    private String title;

    private String path;

    private String icon;

    private String region;

    private String parentId;

    public NavigationVM() {
    }

    public NavigationVM(Navigation navigation) {
        this.id = navigation.getId();
        this.title = navigation.getTitle();
        this.path = navigation.getPath();
        this.icon = navigation.getIcon();
        this.region = navigation.getRegion();
        this.parentId = navigation.getParent().getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
