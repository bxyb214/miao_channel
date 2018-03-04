package com.songzi.channel.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ChannelStatisticsVM {

    @ApiModelProperty(value = "PV")
    private Double pv;

    @ApiModelProperty(value = "UV")
    private Double uv;

    @ApiModelProperty(value = "下单量")
    private Double orderNumber;


    @ApiModelProperty(value = "下单率")
    private Double orderRate;


    @ApiModelProperty(value = "成交量量")
    private Double payNumber;


    @ApiModelProperty(value = "转化率")
    private Double payConversion;


    @ApiModelProperty(value = "订单⾦额")
    private Double salePrice;


    @ApiModelProperty(value = "分成")
    private Double proportionPrice;

    @ApiModelProperty(value = "UV产出")
    private Double uvOutput;


    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "渠道名称")
    private String productName;

    @ApiModelProperty(value = "日期")
    private LocalDate date;

    public Double getPv() {
        return pv;
    }

    public void setPv(Double pv) {
        this.pv = pv;
    }

    public Double getUv() {
        return uv;
    }

    public void setUv(Double uv) {
        this.uv = uv;
    }

    public Double getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Double orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Double getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(Double orderRate) {
        this.orderRate = orderRate;
    }

    public Double getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(Double payNumber) {
        this.payNumber = payNumber;
    }

    public Double getPayConversion() {
        return payConversion;
    }

    public void setPayConversion(Double payConversion) {
        this.payConversion = payConversion;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getProportionPrice() {
        return proportionPrice;
    }

    public void setProportionPrice(Double proportionPrice) {
        this.proportionPrice = proportionPrice;
    }

    public Double getUvOutput() {
        return uvOutput;
    }

    public void setUvOutput(Double uvOutput) {
        this.uvOutput = uvOutput;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
