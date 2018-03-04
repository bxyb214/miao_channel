package com.songzi.channel.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.songzi.channel.domain.enumeration.StatisticsType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ProductStatistics.
 */
@Entity
@Table(name = "statistics")
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "count")
    private Double count;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statistics_type", nullable = false)
    private StatisticsType type;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "channel_code")
    private String channelCode;

    @Column(name = "description")
    private String description;


    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate;

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

    public Statistics name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCount() {
        return count;
    }

    public Statistics count(Double count) {
        this.count = count;
        return this;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public StatisticsType getType() {
        return type;
    }

    public void setType(StatisticsType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public Statistics date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
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
        Statistics statistics = (Statistics) o;
        if (statistics.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statistics.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductStatistics{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", count=" + getCount() +
            ", type=" + getType() +
            "}";
    }
}
