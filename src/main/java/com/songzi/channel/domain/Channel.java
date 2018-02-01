package com.songzi.channel.domain;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Channel.
 */
@Entity
@Table(name = "channel")
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    @ApiModelProperty(value = "渠道代码")
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    @ApiModelProperty(value = "渠道名称")
    private String name;

    @Column(name = "proportion")
    @ApiModelProperty(value = "分成比例")
    private Double proportion;

    @Column(name = "contact_name")
    @ApiModelProperty(value = "联系人")
    private String contactName;

    @Column(name = "contact_phone")
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    @NotNull
    @Column(name = "status", nullable = false)
    @ApiModelProperty(value = "状态")
    private String status;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

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

    public Channel code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Channel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProportion() {
        return proportion;
    }

    public Channel proportion(Double proportion) {
        this.proportion = proportion;
        return this;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    public String getContactName() {
        return contactName;
    }

    public Channel contact_name(String contact_name) {
        this.contactName = contact_name;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public Channel contact_phone(String contact_phone) {
        this.contactPhone = contact_phone;
        return this;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getStatus() {
        return status;
    }

    public Channel status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public Channel user_id(Long user_id) {
        this.userId = user_id;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        Channel channel = (Channel) o;
        if (channel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), channel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Channel{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", proportion=" + getProportion() +
            ", contactName='" + getContactName() + "'" +
            ", contactPhone='" + getContactPhone() + "'" +
            ", status='" + getStatus() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
