package com.songzi.channel.web.rest.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */

@ApiModel(description = "登陆信息")
public class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "用户名", example = "admin", required = true)
    private String username;

    @NotNull
    @Size(min = ManagedUserVM.PASSWORD_MIN_LENGTH, max = ManagedUserVM.PASSWORD_MAX_LENGTH)
    @ApiModelProperty(value = "密码", example = "admin", required = true)
    private String password;

    @ApiModelProperty(value = "记住我", example = "true")
    private Boolean rememberMe;

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

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
            "username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
}
