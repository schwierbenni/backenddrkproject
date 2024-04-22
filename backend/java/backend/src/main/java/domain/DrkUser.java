package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DrkUser.
 */
@Entity
@Table(name = "drk_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DrkUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "last_password_change")
    private Instant lastPasswordChange;

    @Column(name = "password_change_required")
    private Boolean passwordChangeRequired;

    @Column(name = "created_or_edited")
    private Instant createdOrEdited;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "drkUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"drkUser", "protocol"}, allowSetters = true)
    private Set<AdditionalUser> additionalUsers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "drkUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
            value = {"protocolContent", "protocolPdfFile", "additionalUsers", "drkUser", "organization"},
            allowSetters = true
    )
    private Set<Protocol> protocols = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "drkUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"drkUser"}, allowSetters = true)
    private Set<UserSessions> userSessions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "drkUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"drkUser", "organization", "role"}, allowSetters = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"protocols", "users", "userRoles", "templateOrganizations"}, allowSetters = true)
    private Organization organization;

    public Long getId() {
        return this.id;
    }

    public DrkUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public DrkUser userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public DrkUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public DrkUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public DrkUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public DrkUser password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getLastPasswordChange() {
        return this.lastPasswordChange;
    }

    public DrkUser lastPasswordChange(Instant lastPasswordChange) {
        this.setLastPasswordChange(lastPasswordChange);
        return this;
    }

    public void setLastPasswordChange(Instant lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public Boolean getPasswordChangeRequired() {
        return this.passwordChangeRequired;
    }

    public DrkUser passwordChangeRequired(Boolean passwordChangeRequired) {
        this.setPasswordChangeRequired(passwordChangeRequired);
        return this;
    }

    public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public Instant getCreatedOrEdited() {
        return this.createdOrEdited;
    }

    public DrkUser createdOrEdited(Instant createdOrEdited) {
        this.setCreatedOrEdited(createdOrEdited);
        return this;
    }

    public void setCreatedOrEdited(Instant createdOrEdited) {
        this.createdOrEdited = createdOrEdited;
    }

    public Set<AdditionalUser> getAdditionalUsers() {
        return this.additionalUsers;
    }

    public void setAdditionalUsers(Set<AdditionalUser> additionalUsers) {
        if (this.additionalUsers != null) {
            this.additionalUsers.forEach(i -> i.setDrkUser(null));
        }
        if (additionalUsers != null) {
            additionalUsers.forEach(i -> i.setDrkUser(this));
        }
        this.additionalUsers = additionalUsers;
    }

    public DrkUser additionalUsers(Set<AdditionalUser> additionalUsers) {
        this.setAdditionalUsers(additionalUsers);
        return this;
    }

    public DrkUser addAdditionalUser(AdditionalUser additionalUser) {
        this.additionalUsers.add(additionalUser);
        additionalUser.setDrkUser(this);
        return this;
    }

    public DrkUser removeAdditionalUser(AdditionalUser additionalUser) {
        this.additionalUsers.remove(additionalUser);
        additionalUser.setDrkUser(null);
        return this;
    }

    public Set<Protocol> getProtocols() {
        return this.protocols;
    }

    public void setProtocols(Set<Protocol> protocols) {
        if (this.protocols != null) {
            this.protocols.forEach(i -> i.setDrkUser(null));
        }
        if (protocols != null) {
            protocols.forEach(i -> i.setDrkUser(this));
        }
        this.protocols = protocols;
    }

    public DrkUser protocols(Set<Protocol> protocols) {
        this.setProtocols(protocols);
        return this;
    }

    public DrkUser addProtocol(Protocol protocol) {
        this.protocols.add(protocol);
        protocol.setDrkUser(this);
        return this;
    }

    public DrkUser removeProtocol(Protocol protocol) {
        this.protocols.remove(protocol);
        protocol.setDrkUser(null);
        return this;
    }

    public Set<UserSessions> getUserSessions() {
        return this.userSessions;
    }

    public void setUserSessions(Set<UserSessions> userSessions) {
        if (this.userSessions != null) {
            this.userSessions.forEach(i -> i.setDrkUser(null));
        }
        if (userSessions != null) {
            userSessions.forEach(i -> i.setDrkUser(this));
        }
        this.userSessions = userSessions;
    }

    public DrkUser userSessions(Set<UserSessions> userSessions) {
        this.setUserSessions(userSessions);
        return this;
    }

    public DrkUser addUserSessions(UserSessions userSessions) {
        this.userSessions.add(userSessions);
        userSessions.setDrkUser(this);
        return this;
    }

    public DrkUser removeUserSessions(UserSessions userSessions) {
        this.userSessions.remove(userSessions);
        userSessions.setDrkUser(null);
        return this;
    }

    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        if (this.userRoles != null) {
            this.userRoles.forEach(i -> i.setDrkUser(null));
        }
        if (userRoles != null) {
            userRoles.forEach(i -> i.setDrkUser(this));
        }
        this.userRoles = userRoles;
    }

    public DrkUser userRoles(Set<UserRole> userRoles) {
        this.setUserRoles(userRoles);
        return this;
    }

    public DrkUser addUserRole(UserRole userRole) {
        this.userRoles.add(userRole);
        userRole.setDrkUser(this);
        return this;
    }

    public DrkUser removeUserRole(UserRole userRole) {
        this.userRoles.remove(userRole);
        userRole.setDrkUser(null);
        return this;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public DrkUser organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DrkUser)) {
            return false;
        }
        return getId() != null && getId().equals(((DrkUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DrkUser{" +
                "id=" + getId() +
                ", userName='" + getUserName() + "'" +
                ", firstName='" + getFirstName() + "'" +
                ", lastName='" + getLastName() + "'" +
                ", email='" + getEmail() + "'" +
                ", password='" + getPassword() + "'" +
                ", lastPasswordChange='" + getLastPasswordChange() + "'" +
                ", passwordChangeRequired='" + getPasswordChangeRequired() + "'" +
                ", createdOrEdited='" + getCreatedOrEdited() + "'" +
                "}";
    }
}
