package rest;

import domain.UserSessions;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import repository.UserSessionsRepository;
import util.HeaderUtil;
import util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link domain.UserSessions}.
 */
@RestController
@RequestMapping("/api/user-sessions")
@Transactional
public class UserSessionsResource {

    private final Logger log = LoggerFactory.getLogger(UserSessionsResource.class);

    private static final String ENTITY_NAME = "userSessions";

    @Value("${spring.application.name}")
    private String applicationName;

    private final UserSessionsRepository userSessionsRepository;

    public UserSessionsResource(UserSessionsRepository userSessionsRepository) {
        this.userSessionsRepository = userSessionsRepository;
    }

    /**
     * {@code POST  /user-sessions} : Create a new userSessions.
     *
     * @param userSessions the userSessions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSessions, or with status {@code 400 (Bad Request)} if the userSessions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserSessions> createUserSessions(@RequestBody UserSessions userSessions) throws URISyntaxException, BadRequestException {
        log.debug("REST request to save UserSessions : {}", userSessions);
        if (userSessions.getId() != null) {
            throw new BadRequestException("A new userSessions cannot already have an ID");
        }
        UserSessions result = userSessionsRepository.save(userSessions);
        return ResponseEntity
                .created(new URI("/api/user-sessions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /user-sessions/:id} : Updates an existing userSessions.
     *
     * @param id           the id of the userSessions to save.
     * @param userSessions the userSessions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSessions,
     * or with status {@code 400 (Bad Request)} if the userSessions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSessions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserSessions> updateUserSessions(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody UserSessions userSessions
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update UserSessions : {}, {}", id, userSessions);
        if (userSessions.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, userSessions.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!userSessionsRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        UserSessions result = userSessionsRepository.save(userSessions);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSessions.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /user-sessions/:id} : Partial updates given fields of an existing userSessions, field will ignore if it is null
     *
     * @param id           the id of the userSessions to save.
     * @param userSessions the userSessions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSessions,
     * or with status {@code 400 (Bad Request)} if the userSessions is not valid,
     * or with status {@code 404 (Not Found)} if the userSessions is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSessions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<UserSessions> partialUpdateUserSessions(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody UserSessions userSessions
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update UserSessions partially : {}, {}", id, userSessions);
        if (userSessions.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, userSessions.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!userSessionsRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<UserSessions> result = userSessionsRepository.findById(userSessions.getId()).map(userSessionsRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSessions.getId().toString())
        );
    }

    /**
     * {@code GET  /user-sessions} : get all the userSessions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSessions in body.
     */
    @GetMapping("")
    public List<UserSessions> getAllUserSessions() {
        log.debug("REST request to get all UserSessions");
        return userSessionsRepository.findAll();
    }

    /**
     * {@code GET  /user-sessions/:id} : get the "id" userSessions.
     *
     * @param id the id of the userSessions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSessions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserSessions> getUserSessions(@PathVariable("id") Long id) {
        log.debug("REST request to get UserSessions : {}", id);
        Optional<UserSessions> userSessions = userSessionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userSessions);
    }

    /**
     * {@code DELETE  /user-sessions/:id} : delete the "id" userSessions.
     *
     * @param id the id of the userSessions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSessions(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserSessions : {}", id);
        userSessionsRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
