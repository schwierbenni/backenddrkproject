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
 * A Protocol.
 */
@Entity
@Table(name = "protocol")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Protocol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_draft")
    private Boolean isDraft;

    @Column(name = "review_comment")
    private String reviewComment;

    @Column(name = "is_closed")
    private Boolean isClosed;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "created_or_edited")
    private Instant createdOrEdited;

    @JsonIgnoreProperties(value = { "protocol" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ProtocolContent protocolContent;

    @JsonIgnoreProperties(value = { "protocol" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ProtocolPdfFile protocolPdfFile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "protocol")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "drkUser", "protocol" }, allowSetters = true)
    private Set<AdditionalUser> additionalUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "additionalUsers", "protocols", "userSessions", "userRoles", "organization" }, allowSetters = true)
    private DrkUser drkUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "protocols", "users", "userRoles", "templateOrganizations" }, allowSetters = true)
    private Organization organization;

    public Long getId() {
        return this.id;
    }

    public Protocol id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsDraft() {
        return this.isDraft;
    }

    public Protocol isDraft(Boolean isDraft) {
        this.setIsDraft(isDraft);
        return this;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public String getReviewComment() {
        return this.reviewComment;
    }

    public Protocol reviewComment(String reviewComment) {
        this.setReviewComment(reviewComment);
        return this;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public Boolean getIsClosed() {
        return this.isClosed;
    }

    public Protocol isClosed(Boolean isClosed) {
        this.setIsClosed(isClosed);
        return this;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Instant getClosedAt() {
        return this.closedAt;
    }

    public Protocol closedAt(Instant closedAt) {
        this.setClosedAt(closedAt);
        return this;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public Instant getCreatedOrEdited() {
        return this.createdOrEdited;
    }

    public Protocol createdOrEdited(Instant createdOrEdited) {
        this.setCreatedOrEdited(createdOrEdited);
        return this;
    }

    public void setCreatedOrEdited(Instant createdOrEdited) {
        this.createdOrEdited = createdOrEdited;
    }

    public ProtocolContent getProtocolContent() {
        return this.protocolContent;
    }

    public void setProtocolContent(ProtocolContent protocolContent) {
        this.protocolContent = protocolContent;
    }

    public Protocol protocolContent(ProtocolContent protocolContent) {
        this.setProtocolContent(protocolContent);
        return this;
    }

    public ProtocolPdfFile getProtocolPdfFile() {
        return this.protocolPdfFile;
    }

    public void setProtocolPdfFile(ProtocolPdfFile protocolPdfFile) {
        this.protocolPdfFile = protocolPdfFile;
    }

    public Protocol protocolPdfFile(ProtocolPdfFile protocolPdfFile) {
        this.setProtocolPdfFile(protocolPdfFile);
        return this;
    }

    public Set<AdditionalUser> getAdditionalUsers() {
        return this.additionalUsers;
    }

    public void setAdditionalUsers(Set<AdditionalUser> additionalUsers) {
        if (this.additionalUsers != null) {
            this.additionalUsers.forEach(i -> i.setProtocol(null));
        }
        if (additionalUsers != null) {
            additionalUsers.forEach(i -> i.setProtocol(this));
        }
        this.additionalUsers = additionalUsers;
    }

    public Protocol additionalUsers(Set<AdditionalUser> additionalUsers) {
        this.setAdditionalUsers(additionalUsers);
        return this;
    }

    public Protocol addAdditionalUser(AdditionalUser additionalUser) {
        this.additionalUsers.add(additionalUser);
        additionalUser.setProtocol(this);
        return this;
    }

    public Protocol removeAdditionalUser(AdditionalUser additionalUser) {
        this.additionalUsers.remove(additionalUser);
        additionalUser.setProtocol(null);
        return this;
    }

    public DrkUser getDrkUser() {
        return this.drkUser;
    }

    public void setDrkUser(DrkUser drkUser) {
        this.drkUser = drkUser;
    }

    public Protocol drkUser(DrkUser drkUser) {
        this.setDrkUser(drkUser);
        return this;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Protocol organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Protocol)) {
            return false;
        }
        return getId() != null && getId().equals(((Protocol) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Protocol{" +
            "id=" + getId() +
            ", isDraft='" + getIsDraft() + "'" +
            ", reviewComment='" + getReviewComment() + "'" +
            ", isClosed='" + getIsClosed() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", createdOrEdited='" + getCreatedOrEdited() + "'" +
            "}";
    }
}
