package com.chf.app.web.vm;

import java.util.Optional;

import com.chf.app.domain.Organization;

public class OrganizationVM {

    private String id;

    private String name;

    private String parentId;

    private boolean hasChildren;

    public OrganizationVM() {
    }

    public OrganizationVM(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.parentId = Optional.ofNullable(organization.getParent()).map(Organization::getId).orElse(null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

}
