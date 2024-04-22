package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A UserRole.
 */
@Entity
@Table(name = "user_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRole implements Serializable {

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
    @JsonIgnoreProperties(value = { "protocols", "users", "userRoles", "templateOrganizations" }, allowSetters = true)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userRoles" }, allowSetters = true)
    private Role role;

    public Long getId() {
        return this.id;
    }

    public UserRole id(Long id) {
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

    public UserRole drkUser(DrkUser drkUser) {
        this.setDrkUser(drkUser);
        return this;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public UserRole organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserRole role(Role role) {
        this.setRole(role);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRole)) {
            return false;
        }
        return getId() != null && getId().equals(((UserRole) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserRole{" +
            "id=" + getId() +
            "}";
    }
}
