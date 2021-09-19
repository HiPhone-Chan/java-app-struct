package com.chf.app.service.dto;

import java.util.List;

import com.chf.app.domain.Navigation;

public class NavigationTreeDTO {

    private String id;

    private String title;

    private String path;

    private String icon;

    private List<NavigationTreeDTO> children;

    public void set(Navigation nav) {
        this.id = nav.getId();
        this.title = nav.getTitle();
        this.path = nav.getPath();
        this.icon = nav.getIcon();
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

    public List<NavigationTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<NavigationTreeDTO> children) {
        this.children = children;
    }

}
