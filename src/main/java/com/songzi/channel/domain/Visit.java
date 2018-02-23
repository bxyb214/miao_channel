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
@Table(name = "visit", indexes = {
    @Index(columnList = "ip", name = "visit_ip_index")})
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
    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "ip")
    private String ip;


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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
            ", ip=" + getIp() +
            "}";
    }
}
