package com.songzi.channel.domain;


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
public class JhiOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "birth_info")
    private String birthInfo;

    @Column(name = "sex_info")
    private String sexInfo;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "channel_id")
    private Integer channelId;

    @Column(name = "channel_name")
    private String channelName;

    @NotNull
    @Column(name = "pay_type", nullable = false)
    private String payType;

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

    public Integer getProductId() {
        return productId;
    }

    public JhiOrder product_id(Integer product_id) {
        this.productId = product_id;
        return this;
    }

    public void setProductId(Integer productId) {
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

    public String getSexInfo() {
        return sexInfo;
    }

    public JhiOrder sex_info(String sex_info) {
        this.sexInfo = sex_info;
        return this;
    }

    public void setSexInfo(String sexInfo) {
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

    public String getStatus() {
        return status;
    }

    public JhiOrder status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getChannelId() {
        return channelId;
    }

    public JhiOrder channel_id(Integer channel_id) {
        this.channelId = channel_id;
        return this;
    }

    public void setChannelId(Integer channelId) {
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

    public String getPayType() {
        return payType;
    }

    public JhiOrder pay_type(String pay_type) {
        this.payType = pay_type;
        return this;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
