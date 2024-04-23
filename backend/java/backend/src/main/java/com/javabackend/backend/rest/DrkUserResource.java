package com.javabackend.backend.rest;

import com.javabackend.backend.util.HeaderUtil;
import com.javabackend.backend.util.ResponseUtil;
import com.javabackend.backend.domain.DrkUser;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.javabackend.backend.repository.DrkUserRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link DrkUser}.
 */
@RestController
@RequestMapping("/api/drk-users")
@Transactional
public class DrkUserResource {

    private final Logger log = LoggerFactory.getLogger(DrkUserResource.class);

    private static final String ENTITY_NAME = "drkUser";

    @Value("${spring.application.name}")
    private String applicationName;

    private final DrkUserRepository drkUserRepository;

    public DrkUserResource(DrkUserRepository drkUserRepository) {
        this.drkUserRepository = drkUserRepository;
    }

    /**
     * {@code POST  /drk-users} : Create a new drkUser.
     *
     * @param drkUser the drkUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new drkUser, or with status {@code 400 (Bad Request)} if the drkUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<DrkUser> createDrkUser(@RequestBody DrkUser drkUser) throws URISyntaxException, BadRequestException {
        log.debug("REST request to save DrkUser : {}", drkUser);
        if (drkUser.getId() != null) {
            throw new BadRequestException("A new DrkUser cannot already have an ID");
        }
        DrkUser result = drkUserRepository.save(drkUser);
        return ResponseEntity
                .created(new URI("/api/drk-users/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /drk-users/:id} : Updates an existing drkUser.
     *
     * @param id      the id of the drkUser to save.
     * @param drkUser the drkUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated drkUser,
     * or with status {@code 400 (Bad Request)} if the drkUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the drkUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DrkUser> updateDrkUser(@PathVariable(value = "id", required = false) final Long id, @RequestBody DrkUser drkUser)
            throws URISyntaxException, BadRequestException {
        log.debug("REST request to update DrkUser : {}, {}", id, drkUser);
        if (drkUser.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, drkUser.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!drkUserRepository.existsById(id)) {
            throw new BadRequestException("Invalid id");
        }

        DrkUser result = drkUserRepository.save(drkUser);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, drkUser.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /drk-users/:id} : Partial updates given fields of an existing drkUser, field will ignore if it is null
     *
     * @param id      the id of the drkUser to save.
     * @param drkUser the drkUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated drkUser,
     * or with status {@code 400 (Bad Request)} if the drkUser is not valid,
     * or with status {@code 404 (Not Found)} if the drkUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the drkUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<DrkUser> partialUpdateDrkUser(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody DrkUser drkUser
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update DrkUser partially : {}, {}", id, drkUser);
        if (drkUser.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, drkUser.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!drkUserRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<DrkUser> result = drkUserRepository
                .findById(drkUser.getId())
                .map(existingDrkUser -> {
                    if (drkUser.getUserName() != null) {
                        existingDrkUser.setUserName(drkUser.getUserName());
                    }
                    if (drkUser.getFirstName() != null) {
                        existingDrkUser.setFirstName(drkUser.getFirstName());
                    }
                    if (drkUser.getLastName() != null) {
                        existingDrkUser.setLastName(drkUser.getLastName());
                    }
                    if (drkUser.getEmail() != null) {
                        existingDrkUser.setEmail(drkUser.getEmail());
                    }
                    if (drkUser.getPassword() != null) {
                        existingDrkUser.setPassword(drkUser.getPassword());
                    }
                    if (drkUser.getLastPasswordChange() != null) {
                        existingDrkUser.setLastPasswordChange(drkUser.getLastPasswordChange());
                    }
                    if (drkUser.getPasswordChangeRequired() != null) {
                        existingDrkUser.setPasswordChangeRequired(drkUser.getPasswordChangeRequired());
                    }
                    if (drkUser.getCreatedOrEdited() != null) {
                        existingDrkUser.setCreatedOrEdited(drkUser.getCreatedOrEdited());
                    }

                    return existingDrkUser;
                })
                .map(drkUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, drkUser.getId().toString())
        );
    }

    /**
     * {@code GET  /drk-users} : get all the drkUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of drkUsers in body.
     */
    @GetMapping
    public List<DrkUser> getAllDrkUsers() {
        log.debug("REST request to get all DrkUsers");
        return drkUserRepository.findAll();
    }

    /**
     * {@code GET  /drk-users/:id} : get the "id" drkUser.
     *
     * @param id the id of the drkUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the drkUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DrkUser> getDrkUser(@PathVariable("id") Long id) {
        log.debug("REST request to get DrkUser : {}", id);
        Optional<DrkUser> drkUser = drkUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(drkUser);
    }

    /**
     * {@code DELETE  /drk-users/:id} : delete the "id" drkUser.
     *
     * @param id the id of the drkUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrkUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete DrkUser : {}", id);
        drkUserRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
