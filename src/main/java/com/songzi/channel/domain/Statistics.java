package com.songzi.channel.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.songzi.channel.domain.enumeration.StatisticsType;

/**
 * A Statistics.
 */
@Entity
@Table(name = "statistics")
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "count")
    private Integer count;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private StatisticsType type;

    @Column(name = "w_2_w")
    private Double w2w;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

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

    public Integer getCount() {
        return count;
    }

    public Statistics count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public StatisticsType getType() {
        return type;
    }

    public Statistics type(StatisticsType type) {
        this.type = type;
        return this;
    }

    public void setType(StatisticsType type) {
        this.type = type;
    }

    public Double getw2w() {
        return w2w;
    }

    public Statistics w2w(Double w2w) {
        this.w2w = w2w;
        return this;
    }

    public void setw2w(Double w2w) {
        this.w2w = w2w;
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
        return "Statistics{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", count=" + getCount() +
            ", type='" + getType() + "'" +
            ", w2w=" + getw2w() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
