package com.chf.app.web.vm;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.chf.app.domain.User;
import com.chf.app.service.dto.UserDTO;

public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public ManagedUserVM() {
    }

    public ManagedUserVM(User user) {
        super(user);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) && password.length() >= PASSWORD_MIN_LENGTH
                && password.length() <= PASSWORD_MAX_LENGTH;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" + "} " + super.toString();
    }
}
