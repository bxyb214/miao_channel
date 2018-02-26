package com.songzi.channel.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class OrderVM {

    @NotNull
    @ApiModelProperty(value = "产品id")
    private Long productId;

    @NotNull
    @ApiModelProperty(value = "生日信息")
    private String birthInfo;

    @ApiModelProperty(value = "性别")
    private String sexInfo;

    @NotNull
    @ApiModelProperty(value = "金额")
    private Integer price;

    @NotNull
    @ApiModelProperty(value = "渠道id")
    private Long channelId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBirthInfo() {
        return birthInfo;
    }

    public void setBirthInfo(String birthInfo) {
        this.birthInfo = birthInfo;
    }

    public String getSexInfo() {
        return sexInfo;
    }

    public void setSexInfo(String sexInfo) {
        this.sexInfo = sexInfo;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}
