package com.songzi.channel.web.rest.vm;

import javax.validation.constraints.NotNull;

public class PasswordVM {

    private String oldPassword;

    @NotNull
    private String password;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
