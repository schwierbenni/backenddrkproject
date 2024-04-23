package com.javabackend.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A ProtocolPdfFile.
 */
@Entity
@Table(name = "protocol_pdf_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProtocolPdfFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "mime_type")
    private String mimeType;

    @JsonIgnoreProperties(
        value = { "protocolContent", "protocolPdfFile", "additionalUsers", "drkUser", "organization" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "protocolPdfFile")
    private Protocol protocol;


    public Long getId() {
        return this.id;
    }

    public ProtocolPdfFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public ProtocolPdfFile content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public ProtocolPdfFile mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(Protocol protocol) {
        if (this.protocol != null) {
            this.protocol.setProtocolPdfFile(null);
        }
        if (protocol != null) {
            protocol.setProtocolPdfFile(this);
        }
        this.protocol = protocol;
    }

    public ProtocolPdfFile protocol(Protocol protocol) {
        this.setProtocol(protocol);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProtocolPdfFile)) {
            return false;
        }
        return getId() != null && getId().equals(((ProtocolPdfFile) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProtocolPdfFile{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            "}";
    }
}
