package rest;

import domain.TemplateOrganization;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import repository.TemplateOrganizationRepository;
import util.HeaderUtil;
import util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link domain.TemplateOrganization}.
 */
@RestController
@RequestMapping("/api/template-organizations")
@Transactional
public class TemplateOrganizationResource {

    private final Logger log = LoggerFactory.getLogger(TemplateOrganizationResource.class);

    private static final String ENTITY_NAME = "templateOrganization";

    @Value("${spring.application.name}")
    private String applicationName;

    private final TemplateOrganizationRepository templateOrganizationRepository;

    public TemplateOrganizationResource(TemplateOrganizationRepository templateOrganizationRepository) {
        this.templateOrganizationRepository = templateOrganizationRepository;
    }

    /**
     * {@code POST  /template-organizations} : Create a new templateOrganization.
     *
     * @param templateOrganization the templateOrganization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new templateOrganization, or with status {@code 400 (Bad Request)} if the templateOrganization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TemplateOrganization> createTemplateOrganization(@RequestBody TemplateOrganization templateOrganization)
            throws URISyntaxException, BadRequestException {
        log.debug("REST request to save TemplateOrganization : {}", templateOrganization);
        if (templateOrganization.getId() != null) {
            throw new BadRequestException("A new templateOrganization cannot already have an ID");
        }
        TemplateOrganization result = templateOrganizationRepository.save(templateOrganization);
        return ResponseEntity
                .created(new URI("/api/template-organizations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /template-organizations/:id} : Updates an existing templateOrganization.
     *
     * @param id                   the id of the templateOrganization to save.
     * @param templateOrganization the templateOrganization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateOrganization,
     * or with status {@code 400 (Bad Request)} if the templateOrganization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the templateOrganization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TemplateOrganization> updateTemplateOrganization(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody TemplateOrganization templateOrganization
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update TemplateOrganization : {}, {}", id, templateOrganization);
        if (templateOrganization.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, templateOrganization.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!templateOrganizationRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        TemplateOrganization result = templateOrganizationRepository.save(templateOrganization);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateOrganization.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /template-organizations/:id} : Partial updates given fields of an existing templateOrganization, field will ignore if it is null
     *
     * @param id                   the id of the templateOrganization to save.
     * @param templateOrganization the templateOrganization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateOrganization,
     * or with status {@code 400 (Bad Request)} if the templateOrganization is not valid,
     * or with status {@code 404 (Not Found)} if the templateOrganization is not found,
     * or with status {@code 500 (Internal Server Error)} if the templateOrganization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<TemplateOrganization> partialUpdateTemplateOrganization(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody TemplateOrganization templateOrganization
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update TemplateOrganization partially : {}, {}", id, templateOrganization);
        if (templateOrganization.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, templateOrganization.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!templateOrganizationRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<TemplateOrganization> result = templateOrganizationRepository
                .findById(templateOrganization.getId())
                .map(templateOrganizationRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateOrganization.getId().toString())
        );
    }

    /**
     * {@code GET  /template-organizations} : get all the templateOrganizations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templateOrganizations in body.
     */
    @GetMapping("")
    public List<TemplateOrganization> getAllTemplateOrganizations() {
        log.debug("REST request to get all TemplateOrganizations");
        return templateOrganizationRepository.findAll();
    }

    /**
     * {@code GET  /template-organizations/:id} : get the "id" templateOrganization.
     *
     * @param id the id of the templateOrganization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the templateOrganization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TemplateOrganization> getTemplateOrganization(@PathVariable("id") Long id) {
        log.debug("REST request to get TemplateOrganization : {}", id);
        Optional<TemplateOrganization> templateOrganization = templateOrganizationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(templateOrganization);
    }

    /**
     * {@code DELETE  /template-organizations/:id} : delete the "id" templateOrganization.
     *
     * @param id the id of the templateOrganization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplateOrganization(@PathVariable("id") Long id) {
        log.debug("REST request to delete TemplateOrganization : {}", id);
        templateOrganizationRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
