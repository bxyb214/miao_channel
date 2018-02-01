package com.songzi.channel.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.songzi.channel.domain.enumeration.ProductType;

import com.songzi.channel.domain.enumeration.Status;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nice_name")
    private String nice_name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "picture_url")
    private String picture_url;

    @Column(name = "price")
    private Double price;

    @Column(name = "price_order")
    private Double price_order;

    @Column(name = "channel_id")
    private Integer channel_id;

    @Column(name = "jhi_link")
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType product_type;

    @Column(name = "channel_name")
    private String channel_name;

    @Column(name = "sold")
    private Integer sold;

    @Column(name = "price_point")
    private Integer price_point;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

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

    public Double getPrice_order() {
        return price_order;
    }

    public Product price_order(Double price_order) {
        this.price_order = price_order;
        return this;
    }

    public void setPrice_order(Double price_order) {
        this.price_order = price_order;
    }

    public Integer getChannel_id() {
        return channel_id;
    }

    public Product channel_id(Integer channel_id) {
        this.channel_id = channel_id;
        return this;
    }

    public void setChannel_id(Integer channel_id) {
        this.channel_id = channel_id;
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

    public ProductType getProduct_type() {
        return product_type;
    }

    public Product product_type(ProductType product_type) {
        this.product_type = product_type;
        return this;
    }

    public void setProduct_type(ProductType product_type) {
        this.product_type = product_type;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public Product channel_name(String channel_name) {
        this.channel_name = channel_name;
        return this;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
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

    public Integer getPrice_point() {
        return price_point;
    }

    public Product price_point(Integer price_point) {
        this.price_point = price_point;
        return this;
    }

    public void setPrice_point(Integer price_point) {
        this.price_point = price_point;
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
            ", nice_name='" + getNice_name() + "'" +
            ", description='" + getDescription() + "'" +
            ", picture_url='" + getPicture_url() + "'" +
            ", price=" + getPrice() +
            ", price_order=" + getPrice_order() +
            ", channel_id=" + getChannel_id() +
            ", link='" + getLink() + "'" +
            ", product_type='" + getProduct_type() + "'" +
            ", channel_name='" + getChannel_name() + "'" +
            ", sold=" + getSold() +
            ", price_point=" + getPrice_point() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
