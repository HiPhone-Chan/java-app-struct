package com.chf.app.web.vm;

import static com.chf.app.web.vm.ManagedUserVM.PASSWORD_MAX_LENGTH;
import static com.chf.app.web.vm.ManagedUserVM.PASSWORD_MIN_LENGTH;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.chf.app.constants.SystemConstants;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @Pattern(regexp = SystemConstants.LOGIN_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginVM{" + "username='" + username + '\'' + ", rememberMe=" + rememberMe + '}';
    }
}
