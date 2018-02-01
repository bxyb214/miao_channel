package com.songzi.channel.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Visit.
 */
@Entity
@Table(name = "visit")
public class Visit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @NotNull
    @Column(name = "channel_id", nullable = false)
    private Integer channelId;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @Column(name = "pv")
    private Integer pv;

    @Column(name = "uv")
    private Integer uv;

    @Column(name = "sales_price")
    private Double salesPrice;

    @Column(name = "sales_number")
    private Integer salesNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public Visit product_id(Integer product_id) {
        this.productId = product_id;
        return this;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public Visit channel_id(Integer channel_id) {
        this.channelId = channel_id;
        return this;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Visit date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPv() {
        return pv;
    }

    public Visit pv(Integer pv) {
        this.pv = pv;
        return this;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public Visit uv(Integer uv) {
        this.uv = uv;
        return this;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public Visit sales_price(Double sales_price) {
        this.salesPrice = sales_price;
        return this;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Integer getSalesNumber() {
        return salesNumber;
    }

    public Visit sales_number(Integer sales_number) {
        this.salesNumber = sales_number;
        return this;
    }

    public void setSalesNumber(Integer salesNumber) {
        this.salesNumber = salesNumber;
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
        Visit visit = (Visit) o;
        if (visit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), visit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Visit{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", channelId=" + getChannelId() +
            ", date='" + getDate() + "'" +
            ", pv=" + getPv() +
            ", uv=" + getUv() +
            ", salesPrice=" + getSalesPrice() +
            ", salesNumber=" + getSalesNumber() +
            "}";
    }
}
