package com.songzi.channel.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.songzi.channel.domain.enumeration.PersonaType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A Persona.
 */
@Entity
@Table(name = "persona")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "persona_type", nullable = false)
    private PersonaType personaType;

    @Column(name = "count")
    private Integer count;

    @Column(name = "m_2_m")
    private Integer m2m;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;


    @Column(name = "product_id", nullable = false)
    private Long productId;

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

    public Persona name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonaType getPersonaType() {
        return personaType;
    }

    public Persona personaType(PersonaType personaType) {
        this.personaType = personaType;
        return this;
    }

    public void setPersonaType(PersonaType personaType) {
        this.personaType = personaType;
    }

    public Integer getCount() {
        return count;
    }

    public Persona count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getM2m() {
        return m2m;
    }

    public void setM2m(Integer m2m) {
        this.m2m = m2m;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Persona persona = (Persona) o;
        if (persona.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), persona.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Persona{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", personaType='" + getPersonaType() + "'" +
            ", count=" + getCount() +
            ", channelId=" + getChannelId() +
            ", productId=" + getProductId() +

            "}";
    }
}
