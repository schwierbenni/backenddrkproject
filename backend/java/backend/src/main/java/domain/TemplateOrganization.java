package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A TemplateOrganization.
 */
@Entity
@Table(name = "template_organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "protocols", "users", "userRoles", "templateOrganizations" }, allowSetters = true)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "templateOrganizations" }, allowSetters = true)
    private ProtocolTemplate protocolTemplate;

    public Long getId() {
        return this.id;
    }

    public TemplateOrganization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public TemplateOrganization organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    public ProtocolTemplate getProtocolTemplate() {
        return this.protocolTemplate;
    }

    public void setProtocolTemplate(ProtocolTemplate protocolTemplate) {
        this.protocolTemplate = protocolTemplate;
    }

    public TemplateOrganization protocolTemplate(ProtocolTemplate protocolTemplate) {
        this.setProtocolTemplate(protocolTemplate);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateOrganization)) {
            return false;
        }
        return getId() != null && getId().equals(((TemplateOrganization) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "TemplateOrganization{" +
            "id=" + getId() +
            "}";
    }
}
