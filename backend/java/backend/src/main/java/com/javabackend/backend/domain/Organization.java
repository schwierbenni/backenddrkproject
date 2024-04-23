package com.javabackend.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "type")
    private String type;

    @Column(name = "created_or_edited")
    private Instant createdOrEdited;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
            value = {"protocolContent", "protocolPdfFile", "additionalUsers", "drkUser", "organization"},
            allowSetters = true
    )
    private Set<Protocol> protocols = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"additionalUsers", "protocols", "userSessions", "userRoles", "organization"}, allowSetters = true)
    private Set<DrkUser> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"drkUser", "organization", "role"}, allowSetters = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"organization", "protocolTemplate"}, allowSetters = true)
    private Set<TemplateOrganization> templateOrganizations = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public Organization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public Organization parentId(Long parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return this.name;
    }

    public Organization name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Organization address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public Organization city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Organization postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return this.country;
    }

    public Organization country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return this.type;
    }

    public Organization type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getCreatedOrEdited() {
        return this.createdOrEdited;
    }

    public Organization createdOrEdited(Instant createdOrEdited) {
        this.setCreatedOrEdited(createdOrEdited);
        return this;
    }

    public void setCreatedOrEdited(Instant createdOrEdited) {
        this.createdOrEdited = createdOrEdited;
    }

    public Set<Protocol> getProtocols() {
        return this.protocols;
    }

    public void setProtocols(Set<Protocol> protocols) {
        if (this.protocols != null) {
            this.protocols.forEach(i -> i.setOrganization(null));
        }
        if (protocols != null) {
            protocols.forEach(i -> i.setOrganization(this));
        }
        this.protocols = protocols;
    }

    public Organization protocols(Set<Protocol> protocols) {
        this.setProtocols(protocols);
        return this;
    }

    public Organization addProtocol(Protocol protocol) {
        this.protocols.add(protocol);
        protocol.setOrganization(this);
        return this;
    }

    public Organization removeProtocol(Protocol protocol) {
        this.protocols.remove(protocol);
        protocol.setOrganization(null);
        return this;
    }

    public Set<DrkUser> getUsers() {
        return this.users;
    }

    public void setUsers(Set<DrkUser> drkUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.setOrganization(null));
        }
        if (drkUsers != null) {
            drkUsers.forEach(i -> i.setOrganization(this));
        }
        this.users = drkUsers;
    }

    public Organization users(Set<DrkUser> drkUsers) {
        this.setUsers(drkUsers);
        return this;
    }

    public Organization addUser(DrkUser drkUser) {
        this.users.add(drkUser);
        drkUser.setOrganization(this);
        return this;
    }

    public Organization removeUser(DrkUser drkUser) {
        this.users.remove(drkUser);
        drkUser.setOrganization(null);
        return this;
    }

    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        if (this.userRoles != null) {
            this.userRoles.forEach(i -> i.setOrganization(null));
        }
        if (userRoles != null) {
            userRoles.forEach(i -> i.setOrganization(this));
        }
        this.userRoles = userRoles;
    }

    public Organization userRoles(Set<UserRole> userRoles) {
        this.setUserRoles(userRoles);
        return this;
    }

    public Organization addUserRole(UserRole userRole) {
        this.userRoles.add(userRole);
        userRole.setOrganization(this);
        return this;
    }

    public Organization removeUserRole(UserRole userRole) {
        this.userRoles.remove(userRole);
        userRole.setOrganization(null);
        return this;
    }

    public Set<TemplateOrganization> getTemplateOrganizations() {
        return this.templateOrganizations;
    }

    public void setTemplateOrganizations(Set<TemplateOrganization> templateOrganizations) {
        if (this.templateOrganizations != null) {
            this.templateOrganizations.forEach(i -> i.setOrganization(null));
        }
        if (templateOrganizations != null) {
            templateOrganizations.forEach(i -> i.setOrganization(this));
        }
        this.templateOrganizations = templateOrganizations;
    }

    public Organization templateOrganizations(Set<TemplateOrganization> templateOrganizations) {
        this.setTemplateOrganizations(templateOrganizations);
        return this;
    }

    public Organization addTemplateOrganization(TemplateOrganization templateOrganization) {
        this.templateOrganizations.add(templateOrganization);
        templateOrganization.setOrganization(this);
        return this;
    }

    public Organization removeTemplateOrganization(TemplateOrganization templateOrganization) {
        this.templateOrganizations.remove(templateOrganization);
        templateOrganization.setOrganization(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return getId() != null && getId().equals(((Organization) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + getId() +
                ", parentId=" + getParentId() +
                ", name='" + getName() + "'" +
                ", address='" + getAddress() + "'" +
                ", city='" + getCity() + "'" +
                ", postalCode='" + getPostalCode() + "'" +
                ", country='" + getCountry() + "'" +
                ", type='" + getType() + "'" +
                ", createdOrEdited='" + getCreatedOrEdited() + "'" +
                "}";
    }
}
