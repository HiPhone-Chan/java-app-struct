package com.chf.app.service.bpm.identity;

import org.camunda.bpm.engine.identity.Group;

import com.chf.app.domain.Organization;

public class BpmGroup implements Group {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String type;

    public BpmGroup() {
    }

    public BpmGroup(Organization org) {
        super();
        this.id = org.getId();
        this.name = org.getName();
        this.type = org.getType();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String string) {
        this.type = type;
    }

}
