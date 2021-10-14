package com.chf.app.web.vm;

import java.util.Optional;

import com.chf.app.domain.Navigation;

public class NavigationVM {

    private String id;

    private String title;

    private String path;

    private String icon;

    private String region;

    private String parentId;

    private Integer priority;

    private boolean hasChildren;

    private boolean hasApis;

    public NavigationVM() {
    }

    public NavigationVM(Navigation navigation) {
        this.id = navigation.getId();
        this.title = navigation.getTitle();
        this.path = navigation.getPath();
        this.icon = navigation.getIcon();
        this.priority = navigation.getPriority();
        this.region = navigation.getRegion();
        this.parentId = Optional.ofNullable(navigation.getParent()).map(Navigation::getId).orElse(null);
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isHasApis() {
        return hasApis;
    }

    public void setHasApis(boolean hasApis) {
        this.hasApis = hasApis;
    }

}
