package com.songzi.channel.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.songzi.channel.domain.enumeration.OrderStatus;
import com.songzi.channel.domain.enumeration.PayType;
import com.songzi.channel.domain.enumeration.SexType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A JhiOrder.
 */
@Entity
@Table(name = "jhi_order")
@ApiModel(description = "订单")
public class JhiOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    @ApiModelProperty(value = "订单号")
    private String code;

    @NotNull
    @Column(name = "product_id", nullable = false)
    @ApiModelProperty(value = "产品id")
    private Long productId;

    @NotNull
    @Column(name = "product_name", nullable = false)
    @ApiModelProperty(value = "产品名称")
    private String productName;

    @Column(name = "birth_info")
    @ApiModelProperty(value = "生日信息")
    private String birthInfo;

    @Column(name = "sex_info")
    @ApiModelProperty(value = "性别")
    @Enumerated(EnumType.STRING)
    private SexType sexInfo;

    @NotNull
    @Column(name = "price", nullable = false)
    @ApiModelProperty(value = "金额")
    private Double price;

    @NotNull
    @Column(name = "proportion_price", nullable = false)
    @ApiModelProperty(value = "分成金额")
    private Double proportionPrice;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "状态")
    private OrderStatus status;

    @Column(name = "order_date")
    @ApiModelProperty(value = "购买时间")
    private Instant orderDate;

    @Column(name = "channel_id")
    @ApiModelProperty(value = "渠道id")
    private Long channelId;

    @Column(name = "channel_name")
    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @NotNull
    @Column(name = "pay_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "付款方式")
    private PayType payType;

    @JsonIgnore
    @Column(name = "ip", nullable = false)
    private String ip;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public JhiOrder code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getProductId() {
        return productId;
    }

    public JhiOrder product_id(Long product_id) {
        this.productId = product_id;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public JhiOrder product_name(String product_name) {
        this.productName = product_name;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBirthInfo() {
        return birthInfo;
    }

    public JhiOrder birth_info(String birth_info) {
        this.birthInfo = birth_info;
        return this;
    }

    public void setBirthInfo(String birthInfo) {
        this.birthInfo = birthInfo;
    }

    public SexType getSexInfo() {
        return sexInfo;
    }

    public JhiOrder sex_info(SexType sex_info) {
        this.sexInfo = sex_info;
        return this;
    }

    public void setSexInfo(SexType sexInfo) {
        this.sexInfo = sexInfo;
    }

    public Double getPrice() {
        return price;
    }

    public JhiOrder price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public JhiOrder order_date(Instant order_date) {
        this.orderDate = order_date;
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Long getChannelId() {
        return channelId;
    }

    public JhiOrder channel_id(Long channel_id) {
        this.channelId = channel_id;
        return this;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public JhiOrder channel_name(String channel_name) {
        this.channelName = channel_name;
        return this;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public Double getProportionPrice() {
        return proportionPrice;
    }

    public void setProportionPrice(Double proportionPrice) {
        this.proportionPrice = proportionPrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JhiOrder jhi_order = (JhiOrder) o;
        if (jhi_order.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jhi_order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JhiOrder{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", productId=" + getProductId() +
            ", productName='" + getProductName() + "'" +
            ", birthInfo='" + getBirthInfo() + "'" +
            ", sexInfo='" + getSexInfo() + "'" +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", channelId=" + getChannelId() +
            ", channelName='" + getChannelName() + "'" +
            ", payType='" + getPayType() + "'" +
            "}";
    }
}
