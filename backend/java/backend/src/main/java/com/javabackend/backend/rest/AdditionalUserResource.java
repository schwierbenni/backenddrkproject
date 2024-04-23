package com.javabackend.backend.rest;

import com.javabackend.backend.util.HeaderUtil;
import com.javabackend.backend.util.ResponseUtil;
import com.javabackend.backend.domain.AdditionalUser;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.javabackend.backend.repository.AdditionalUserRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link AdditionalUser}.
 */
@RequestMapping("/api/additional-users")
@Transactional
public class AdditionalUserResource {

    private final Logger log = LoggerFactory.getLogger(AdditionalUserResource.class);

    private static final String ENTITY_NAME = "additionalUser";

    @Value("${spring.application.name}")
    private String applicationName;

    private final AdditionalUserRepository additionalUserRepository;

    public AdditionalUserResource(AdditionalUserRepository additionalUserRepository) {
        this.additionalUserRepository = additionalUserRepository;
    }

    /**
     * {@code POST  /additional-users} : Create a new additionalUser.
     *
     * @param additionalUser the additionalUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new additionalUser, or with status {@code 400 (Bad Request)} if the additionalUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdditionalUser> createAdditionalUser(@RequestBody AdditionalUser additionalUser) throws URISyntaxException, BadRequestException {
        log.debug("REST request to save AdditionalUser : {}", additionalUser);
        if (additionalUser.getId() != null) {
            throw new BadRequestException("A new additionalUser cannot already have an ID");
        }
        AdditionalUser result = additionalUserRepository.save(additionalUser);
        return ResponseEntity
            .created(new URI("/api/additional-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /additional-users/:id} : Updates an existing additionalUser.
     *
     * @param id the id of the additionalUser to save.
     * @param additionalUser the additionalUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalUser,
     * or with status {@code 400 (Bad Request)} if the additionalUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the additionalUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalUser> updateAdditionalUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdditionalUser additionalUser
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update AdditionalUser : {}, {}", id, additionalUser);
        if (additionalUser.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, additionalUser.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!additionalUserRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        AdditionalUser result = additionalUserRepository.save(additionalUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, additionalUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /additional-users/:id} : Partial updates given fields of an existing additionalUser, field will ignore if it is null
     *
     * @param id the id of the additionalUser to save.
     * @param additionalUser the additionalUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalUser,
     * or with status {@code 400 (Bad Request)} if the additionalUser is not valid,
     * or with status {@code 404 (Not Found)} if the additionalUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the additionalUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdditionalUser> partialUpdateAdditionalUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdditionalUser additionalUser
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update AdditionalUser partially : {}, {}", id, additionalUser);
        if (additionalUser.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, additionalUser.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!additionalUserRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<AdditionalUser> result = additionalUserRepository.findById(additionalUser.getId()).map(additionalUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, additionalUser.getId().toString())
        );
    }

    /**
     * {@code GET  /additional-users} : get all the additionalUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of additionalUsers in body.
     */
    @GetMapping("")
    public List<AdditionalUser> getAllAdditionalUsers() {
        log.debug("REST request to get all AdditionalUsers");
        return additionalUserRepository.findAll();
    }

    /**
     * {@code GET  /additional-users/:id} : get the "id" additionalUser.
     *
     * @param id the id of the additionalUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the additionalUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalUser> getAdditionalUser(@PathVariable("id") Long id) {
        log.debug("REST request to get AdditionalUser : {}", id);
        Optional<AdditionalUser> additionalUser = additionalUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(additionalUser);
    }

    /**
     * {@code DELETE  /additional-users/:id} : delete the "id" additionalUser.
     *
     * @param id the id of the additionalUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdditionalUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete AdditionalUser : {}", id);
        additionalUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
