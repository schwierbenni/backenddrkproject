package com.javabackend.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A AdditionalUser.
 */
@Entity
@Table(name = "additional_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdditionalUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "additionalUsers", "protocols", "userSessions", "userRoles", "organization" }, allowSetters = true)
    private DrkUser drkUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "protocolContent", "protocolPdfFile", "additionalUsers", "drkUser", "organization" },
        allowSetters = true
    )
    private Protocol protocol;

    public Long getId() {
        return this.id;
    }

    public AdditionalUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DrkUser getDrkUser() {
        return this.drkUser;
    }

    public void setDrkUser(DrkUser drkUser) {
        this.drkUser = drkUser;
    }

    public AdditionalUser drkUser(DrkUser drkUser) {
        this.setDrkUser(drkUser);
        return this;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public AdditionalUser protocol(Protocol protocol) {
        this.setProtocol(protocol);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdditionalUser)) {
            return false;
        }
        return getId() != null && getId().equals(((AdditionalUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AdditionalUser{" +
            "id=" + getId() +
            "}";
    }
}
