package com.songzi.channel.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.songzi.channel.domain.enumeration.ProductType;

import com.songzi.channel.domain.enumeration.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.BatchSize;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@ApiModel(description = "测试产品")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @ApiModelProperty(value = "测试名称", required = true, example = "test")
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "nice_name")
    @ApiModelProperty(value = "测试简称", required = true, example = "test")
    private String nice_name;

    @Lob
    @Column(name = "description")
    @ApiModelProperty(value = "测试描述", required = true, example = "testtest")
    private String description;

    @Column(name = "picture_url")
    @ApiModelProperty(value = "宣传图", required = true, example = "http://www.baidu.com")
    private String picture_url;

    @Column(name = "price")
    @ApiModelProperty(value = "价格（现价）", required = true, example = "123")
    private Double price;

    @Column(name = "price_order")
    @ApiModelProperty(value = "价格（原价）", required = true, example = "111")
    private Double priceOrder;

    @Column(name = "jhi_link")
    @ApiModelProperty(value = "测试连接", required = true, example = "http://www.baidu.com")
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    @ApiModelProperty(value = "类型")
    private ProductType productType;

    @Column(name = "sold")
    @ApiModelProperty(value = "已出售数")
    private Integer sold;

    @Column(name = "price_point")
    @ApiModelProperty(value = "购买积分")
    private Integer pricePoint;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @ApiModelProperty(value = "价格")
    private Status status;


    @ManyToMany
    @JoinTable(
        name = "product_channel",
        joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "channel_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    @ApiModelProperty(value = "关联渠道")
    private Set<Channel> channels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNice_name() {
        return nice_name;
    }

    public Product nice_name(String nice_name) {
        this.nice_name = nice_name;
        return this;
    }

    public void setNice_name(String nice_name) {
        this.nice_name = nice_name;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public Product picture_url(String picture_url) {
        this.picture_url = picture_url;
        return this;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public Double getPrice() {
        return price;
    }

    public Product price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceOrder() {
        return priceOrder;
    }

    public Product price_order(Double price_order) {
        this.priceOrder = price_order;
        return this;
    }

    public void setPriceOrder(Double priceOrder) {
        this.priceOrder = priceOrder;
    }


    public String getLink() {
        return link;
    }

    public Product link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Product product_type(ProductType product_type) {
        this.productType = product_type;
        return this;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Integer getSold() {
        return sold;
    }

    public Product sold(Integer sold) {
        this.sold = sold;
        return this;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getPricePoint() {
        return pricePoint;
    }

    public Product price_point(Integer price_point) {
        this.pricePoint = price_point;
        return this;
    }

    public void setPricePoint(Integer pricePoint) {
        this.pricePoint = pricePoint;
    }

    public Status getStatus() {
        return status;
    }

    public Product status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", niceName='" + getNice_name() + "'" +
            ", description='" + getDescription() + "'" +
            ", pictureUrl='" + getPicture_url() + "'" +
            ", price=" + getPrice() +
            ", priceOrder=" + getPriceOrder() +
            ", link='" + getLink() + "'" +
            ", productType='" + getProductType() + "'" +
            ", sold=" + getSold() +
            ", pricePoint=" + getPricePoint() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
