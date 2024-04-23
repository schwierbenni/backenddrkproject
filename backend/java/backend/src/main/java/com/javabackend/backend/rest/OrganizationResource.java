package com.javabackend.backend.rest;

import com.javabackend.backend.util.HeaderUtil;
import com.javabackend.backend.util.ResponseUtil;
import com.javabackend.backend.domain.Organization;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.javabackend.backend.repository.OrganizationRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Organization}.
 */
@RestController
@RequestMapping("/api/organizations")
@Transactional
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    private static final String ENTITY_NAME = "organization";

    @Value("${spring.application.name}")
    private String applicationName;

    private final OrganizationRepository organizationRepository;

    public OrganizationResource(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * {@code POST  /organizations} : Create a new organization.
     *
     * @param organization the organization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organization, or with status {@code 400 (Bad Request)} if the organization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) throws URISyntaxException, BadRequestException {
        log.debug("REST request to save Organization : {}", organization);
        if (organization.getId() != null) {
            throw new BadRequestException("A new organization cannot already have an id");
        }
        Organization result = organizationRepository.save(organization);
        return ResponseEntity
                .created(new URI("/api/organizations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /organizations/:id} : Updates an existing organization.
     *
     * @param id           the id of the organization to save.
     * @param organization the organization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organization,
     * or with status {@code 400 (Bad Request)} if the organization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Organization organization
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update Organization : {}, {}", id, organization);
        if (organization.getId() == null) {
            throw new BadRequestException("Invalid ID");
        }
        if (!Objects.equals(id, organization.getId())) {
            throw new BadRequestException("Invalid ID");
        }

        if (!organizationRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Organization result = organizationRepository.save(organization);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organization.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /organizations/:id} : Partial updates given fields of an existing organization, field will ignore if it is null
     *
     * @param id           the id of the organization to save.
     * @param organization the organization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organization,
     * or with status {@code 400 (Bad Request)} if the organization is not valid,
     * or with status {@code 404 (Not Found)} if the organization is not found,
     * or with status {@code 500 (Internal Server Error)} if the organization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<Organization> partialUpdateOrganization(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Organization organization
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update Organization partially : {}, {}", id, organization);
        if (organization.getId() == null) {
            throw new BadRequestException("Invalid ID");
        }
        if (!Objects.equals(id, organization.getId())) {
            throw new BadRequestException("Invalid ID");
        }

        if (!organizationRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<Organization> result = organizationRepository
                .findById(organization.getId())
                .map(existingOrganization -> {
                    if (organization.getParentId() != null) {
                        existingOrganization.setParentId(organization.getParentId());
                    }
                    if (organization.getName() != null) {
                        existingOrganization.setName(organization.getName());
                    }
                    if (organization.getAddress() != null) {
                        existingOrganization.setAddress(organization.getAddress());
                    }
                    if (organization.getCity() != null) {
                        existingOrganization.setCity(organization.getCity());
                    }
                    if (organization.getPostalCode() != null) {
                        existingOrganization.setPostalCode(organization.getPostalCode());
                    }
                    if (organization.getCountry() != null) {
                        existingOrganization.setCountry(organization.getCountry());
                    }
                    if (organization.getType() != null) {
                        existingOrganization.setType(organization.getType());
                    }
                    if (organization.getCreatedOrEdited() != null) {
                        existingOrganization.setCreatedOrEdited(organization.getCreatedOrEdited());
                    }

                    return existingOrganization;
                })
                .map(organizationRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organization.getId().toString())
        );
    }

    /**
     * {@code GET  /organizations} : get all the organizations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizations in body.
     */
    @GetMapping
    public List<Organization> getAllOrganizations() {
        log.debug("REST request to get all Organizations");
        return organizationRepository.findAll();
    }

    /**
     * {@code GET  /organizations/:id} : get the "id" organization.
     *
     * @param id the id of the organization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable("id") Long id) {
        log.debug("REST request to get Organization : {}", id);
        Optional<Organization> organization = organizationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(organization);
    }

    /**
     * {@code DELETE  /organizations/:id} : delete the "id" organization.
     *
     * @param id the id of the organization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable("id") Long id) {
        log.debug("REST request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
