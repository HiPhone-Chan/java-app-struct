package com.chf.app.service.bpm.identity;

import com.chf.app.domain.User;

public class BpmUser implements org.camunda.bpm.engine.identity.User {

    private static final long serialVersionUID = 1L;

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    public BpmUser() {
    }

    public BpmUser(User user) {
        super();
        this.id = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
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
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

}
