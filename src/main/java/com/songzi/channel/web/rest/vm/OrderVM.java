package com.songzi.channel.web.rest.vm;

import com.songzi.channel.domain.enumeration.SexType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class OrderVM {

    @NotNull
    @ApiModelProperty(value = "产品id", example = "9b9a3c771efd11e8b43a060400ef5315")
    private String productId;

    @NotNull
    @ApiModelProperty(value = "生日信息", example = "1984-02-14")
    private String birthInfo;

    @NotNull
    @ApiModelProperty(value = "性别", example = "MALE")
    private SexType sexInfo;

    @ApiModelProperty(value = "token", example = "token")
    private String token;

    @ApiModelProperty(value = "parameter", example = "parameter")
    private String parameter;

    @ApiModelProperty(value = "result", example = "result")
    private String result;

    @NotNull
    @ApiModelProperty(value = "渠道id", example = "9b9a3c751efd11e8b43a060400ef5315")
    private String channelId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBirthInfo() {
        return birthInfo;
    }

    public void setBirthInfo(String birthInfo) {
        this.birthInfo = birthInfo;
    }

    public SexType getSexInfo() {
        return sexInfo;
    }

    public void setSexInfo(SexType sexInfo) {
        this.sexInfo = sexInfo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
