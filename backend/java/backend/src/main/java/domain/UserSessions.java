package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A UserSessions.
 */
@Entity
@Table(name = "user_sessions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSessions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "additionalUsers", "protocols", "userSessions", "userRoles", "organization" }, allowSetters = true)
    private DrkUser drkUser;


    public Long getId() {
        return this.id;
    }

    public UserSessions id(Long id) {
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

    public UserSessions drkUser(DrkUser drkUser) {
        this.setDrkUser(drkUser);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSessions)) {
            return false;
        }
        return getId() != null && getId().equals(((UserSessions) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserSessions{" +
            "id=" + getId() +
            "}";
    }
}
