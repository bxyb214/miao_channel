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
    private Integer pv;

    @Column(name = "uv")
    @ApiModelProperty(value = "UV")
    private Integer uv;

    @Column(name = "order_number")
    @ApiModelProperty(value = "下单量")
    private Integer orderNumber;


    @Column(name = "order_rate")
    @ApiModelProperty(value = "下单率")
    private Integer orderRate;


    @Column(name = "pay_number")
    @ApiModelProperty(value = "成交量量")
    private Integer payNumber;


    @Column(name = "pay_conversion")
    @ApiModelProperty(value = "转化率")
    private Integer payConversion;


    @Column(name = "sale_price")
    @ApiModelProperty(value = "订单⾦额")
    private Integer salePrice;


    @Column(name = "proportion_price")
    @ApiModelProperty(value = "分成")
    private Integer proportionPrice;

    @Column(name = "uv_output")
    @ApiModelProperty(value = "UV产出")
    private Integer uvOutput;


    @NotNull
    @JsonIgnore
    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(Integer orderRate) {
        this.orderRate = orderRate;
    }

    public Integer getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(Integer payNumber) {
        this.payNumber = payNumber;
    }

    public Integer getPayConversion() {
        return payConversion;
    }

    public void setPayConversion(Integer payConversion) {
        this.payConversion = payConversion;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getProportionPrice() {
        return proportionPrice;
    }

    public void setProportionPrice(Integer proportionPrice) {
        this.proportionPrice = proportionPrice;
    }

    public Integer getUvOutput() {
        return uvOutput;
    }

    public void setUvOutput(Integer uvOutput) {
        this.uvOutput = uvOutput;
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
}
