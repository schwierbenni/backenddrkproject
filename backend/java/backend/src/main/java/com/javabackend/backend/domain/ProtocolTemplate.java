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
 * A ProtocolTemplate.
 */
@Entity
@Table(name = "protocol_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProtocolTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "template")
    private String template;

    @Column(name = "created_or_edited")
    private Instant createdOrEdited;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "protocolTemplate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organization", "protocolTemplate" }, allowSetters = true)
    private Set<TemplateOrganization> templateOrganizations = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public ProtocolTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProtocolTemplate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProtocolTemplate description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplate() {
        return this.template;
    }

    public ProtocolTemplate template(String template) {
        this.setTemplate(template);
        return this;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Instant getCreatedOrEdited() {
        return this.createdOrEdited;
    }

    public ProtocolTemplate createdOrEdited(Instant createdOrEdited) {
        this.setCreatedOrEdited(createdOrEdited);
        return this;
    }

    public void setCreatedOrEdited(Instant createdOrEdited) {
        this.createdOrEdited = createdOrEdited;
    }

    public Set<TemplateOrganization> getTemplateOrganizations() {
        return this.templateOrganizations;
    }

    public void setTemplateOrganizations(Set<TemplateOrganization> templateOrganizations) {
        if (this.templateOrganizations != null) {
            this.templateOrganizations.forEach(i -> i.setProtocolTemplate(null));
        }
        if (templateOrganizations != null) {
            templateOrganizations.forEach(i -> i.setProtocolTemplate(this));
        }
        this.templateOrganizations = templateOrganizations;
    }

    public ProtocolTemplate templateOrganizations(Set<TemplateOrganization> templateOrganizations) {
        this.setTemplateOrganizations(templateOrganizations);
        return this;
    }

    public ProtocolTemplate addTemplateOrganization(TemplateOrganization templateOrganization) {
        this.templateOrganizations.add(templateOrganization);
        templateOrganization.setProtocolTemplate(this);
        return this;
    }

    public ProtocolTemplate removeTemplateOrganization(TemplateOrganization templateOrganization) {
        this.templateOrganizations.remove(templateOrganization);
        templateOrganization.setProtocolTemplate(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProtocolTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((ProtocolTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProtocolTemplate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", template='" + getTemplate() + "'" +
            ", createdOrEdited='" + getCreatedOrEdited() + "'" +
            "}";
    }
}
