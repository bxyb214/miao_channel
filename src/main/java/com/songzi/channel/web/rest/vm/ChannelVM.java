package com.songzi.channel.web.rest.vm;


import com.songzi.channel.domain.Channel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "渠道入参", description = "渠道入参")
public class ChannelVM {

    @ApiModelProperty(value = "渠道", required = true)
    @NotNull
    private Channel channel;

    @ApiModelProperty(value = "登陆用户名", required = true)
    @NotNull
    private String login;

    @ApiModelProperty(value = "密码", required = true)
    @NotNull
    private String password;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
