package com.chf.app.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.chf.app.domain.User;

public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String nickName;

    private String mobile;

    @Size(max = 256)
    private String imageUrl;

    public UserDTO() {
    }

    public UserDTO(User user) {
        super();
        this.id = user.getId();
        this.login = user.getLogin();
        this.nickName = user.getNickName();
        this.mobile = user.getMobile();
        this.imageUrl = user.getImageUrl();
    }

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setNickName(nickName);
        user.setMobile(mobile);
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDTO{" + "id='" + id + '\'' + ", login='" + login + '\'' + "}";
    }
}
