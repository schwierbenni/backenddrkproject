package rest;

import domain.UserRole;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import repository.UserRoleRepository;
import util.HeaderUtil;
import util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link domain.UserRole}.
 */
@RestController
@RequestMapping("/api/user-roles")
@Transactional
public class UserRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);

    private static final String ENTITY_NAME = "userRole";

    @Value("${spring.application.name}")
    private String applicationName;

    private final UserRoleRepository userRoleRepository;

    public UserRoleResource(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * {@code POST  /user-roles} : Create a new userRole.
     *
     * @param userRole the userRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRole, or with status {@code 400 (Bad Request)} if the userRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserRole> createUserRole(@RequestBody UserRole userRole) throws URISyntaxException, BadRequestException {
        log.debug("REST request to save UserRole : {}", userRole);
        if (userRole.getId() != null) {
            throw new BadRequestException("A new userRole cannot already have an ID");
        }
        UserRole result = userRoleRepository.save(userRole);
        return ResponseEntity
            .created(new URI("/api/user-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-roles/:id} : Updates an existing userRole.
     *
     * @param id the id of the userRole to save.
     * @param userRole the userRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRole,
     * or with status {@code 400 (Bad Request)} if the userRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserRole> updateUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRole userRole
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update UserRole : {}, {}", id, userRole);
        if (userRole.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, userRole.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!userRoleRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        UserRole result = userRoleRepository.save(userRole);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userRole.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-roles/:id} : Partial updates given fields of an existing userRole, field will ignore if it is null
     *
     * @param id the id of the userRole to save.
     * @param userRole the userRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRole,
     * or with status {@code 400 (Bad Request)} if the userRole is not valid,
     * or with status {@code 404 (Not Found)} if the userRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the userRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserRole> partialUpdateUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRole userRole
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update UserRole partially : {}, {}", id, userRole);
        if (userRole.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, userRole.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!userRoleRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<UserRole> result = userRoleRepository.findById(userRole.getId()).map(userRoleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userRole.getId().toString())
        );
    }

    /**
     * {@code GET  /user-roles} : get all the userRoles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userRoles in body.
     */
    @GetMapping("")
    public List<UserRole> getAllUserRoles() {
        log.debug("REST request to get all UserRoles");
        return userRoleRepository.findAll();
    }

    /**
     * {@code GET  /user-roles/:id} : get the "id" userRole.
     *
     * @param id the id of the userRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserRole> getUserRole(@PathVariable("id") Long id) {
        log.debug("REST request to get UserRole : {}", id);
        Optional<UserRole> userRole = userRoleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userRole);
    }

    /**
     * {@code DELETE  /user-roles/:id} : delete the "id" userRole.
     *
     * @param id the id of the userRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserRole : {}", id);
        userRoleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
