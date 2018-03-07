package com.songzi.channel.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ProductStatistics.
 */
@Entity
@Table(name = "channel_statistics")
@ApiModel(description = "渠道统计")
public class ChannelStatistics implements Serializable {

    /**
     * PV = ⻚面浏览量。
     UV = ⼀天内（00:00 - 24:00）打开测试⻚面的客户端数量。
     下单量 = ⽣成订单数（包括付款和未付款）
     下单率 = 下单量/UV
     成交量量 = 付款成功的订单数
     转化率 = 成交量/UV
     订单⾦额 = 付款成功的订单总⾦额
     分成 = 订单⾦额*分成⽐例
     UV产出 = 收⼊/UV
     */

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "pv", nullable = false)
    @ApiModelProperty(value = "PV")
    private Double pv;

    @Column(name = "uv")
    @ApiModelProperty(value = "UV")
    private Double uv;

    @Column(name = "order_number")
    @ApiModelProperty(value = "下单量")
    private Double orderNumber;


    @Column(name = "order_rate")
    @ApiModelProperty(value = "下单率")
    private Double orderRate;


    @Column(name = "pay_number")
    @ApiModelProperty(value = "成交量量")
    private Double payNumber;


    @Column(name = "pay_conversion")
    @ApiModelProperty(value = "转化率")
    private Double payConversion;


    @Column(name = "sale_price")
    @ApiModelProperty(value = "订单⾦额")
    private Double salePrice;


    @Column(name = "proportion_price")
    @ApiModelProperty(value = "分成")
    private Double proportionPrice;

    @Column(name = "uv_output")
    @ApiModelProperty(value = "UV产出")
    private Double uvOutput;

    @NotNull
    @JsonIgnore
    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @NotNull
    @Column(name = "channel_name", nullable = false)
    private String channelName;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @JsonIgnore
    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}
