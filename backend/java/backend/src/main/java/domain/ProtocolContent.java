package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A ProtocolContent.
 */
@Entity
@Table(name = "protocol_content")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProtocolContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @JsonIgnoreProperties(
        value = { "protocolContent", "protocolPdfFile", "additionalUsers", "drkUser", "organization" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "protocolContent")
    private Protocol protocol;

    public Long getId() {
        return this.id;
    }

    public ProtocolContent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public ProtocolContent content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(Protocol protocol) {
        if (this.protocol != null) {
            this.protocol.setProtocolContent(null);
        }
        if (protocol != null) {
            protocol.setProtocolContent(this);
        }
        this.protocol = protocol;
    }

    public ProtocolContent protocol(Protocol protocol) {
        this.setProtocol(protocol);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProtocolContent)) {
            return false;
        }
        return getId() != null && getId().equals(((ProtocolContent) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProtocolContent{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            "}";
    }
}
