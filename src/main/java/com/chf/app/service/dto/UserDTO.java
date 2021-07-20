package com.chf.app.service.dto;

import javax.validation.constraints.Email;
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

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private String mobile;

    @Size(max = 256)
    private String imageUrl;

    @Size(min = 2, max = 5)
    private String langKey;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getId(), user.getLogin(), user.getNickName(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getMobile(), user.getImageUrl(), user.getLangKey());
    }

    public UserDTO(Long id, @NotBlank @Size(min = 1, max = 50) String login, @Size(max = 50) String nickName,
            @Size(max = 50) String firstName, @Size(max = 50) String lastName,
            @Email @Size(min = 5, max = 100) String email, String mobile, @Size(max = 256) String imageUrl,
            @Size(min = 2, max = 5) String langKey) {
        super();
        this.id = id;
        this.login = login;
        this.nickName = nickName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.imageUrl = imageUrl;
        this.langKey = langKey;
    }

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setNickName(nickName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", login=" + login + ", nickName=" + nickName + ", firstName=" + firstName
                + ", lastName=" + lastName + ", email=" + email + ", mobile=" + mobile + ", imageUrl=" + imageUrl
                + ", langKey=" + langKey + "]";
    }
}
