package rest;

import domain.ProtocolTemplate;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import repository.ProtocolTemplateRepository;
import util.HeaderUtil;
import util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link domain.ProtocolTemplate}.
 */
@RestController
@RequestMapping("/api/protocol-templates")
@Transactional
public class ProtocolTemplateResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolTemplateResource.class);

    private static final String ENTITY_NAME = "protocolTemplate";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ProtocolTemplateRepository protocolTemplateRepository;

    public ProtocolTemplateResource(ProtocolTemplateRepository protocolTemplateRepository) {
        this.protocolTemplateRepository = protocolTemplateRepository;
    }

    /**
     * {@code POST  /protocol-templates} : Create a new protocolTemplate.
     *
     * @param protocolTemplate the protocolTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protocolTemplate, or with status {@code 400 (Bad Request)} if the protocolTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProtocolTemplate> createProtocolTemplate(@RequestBody ProtocolTemplate protocolTemplate)
            throws URISyntaxException, BadRequestException {
        log.debug("REST request to save ProtocolTemplate : {}", protocolTemplate);
        if (protocolTemplate.getId() != null) {
            throw new BadRequestException("A new protocolTemplate cannot already have an ID");
        }
        ProtocolTemplate result = protocolTemplateRepository.save(protocolTemplate);
        return ResponseEntity
            .created(new URI("/api/protocol-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /protocol-templates/:id} : Updates an existing protocolTemplate.
     *
     * @param id the id of the protocolTemplate to save.
     * @param protocolTemplate the protocolTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolTemplate,
     * or with status {@code 400 (Bad Request)} if the protocolTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protocolTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProtocolTemplate> updateProtocolTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProtocolTemplate protocolTemplate
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update ProtocolTemplate : {}, {}", id, protocolTemplate);
        if (protocolTemplate.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, protocolTemplate.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!protocolTemplateRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        ProtocolTemplate result = protocolTemplateRepository.save(protocolTemplate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocolTemplate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /protocol-templates/:id} : Partial updates given fields of an existing protocolTemplate, field will ignore if it is null
     *
     * @param id the id of the protocolTemplate to save.
     * @param protocolTemplate the protocolTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolTemplate,
     * or with status {@code 400 (Bad Request)} if the protocolTemplate is not valid,
     * or with status {@code 404 (Not Found)} if the protocolTemplate is not found,
     * or with status {@code 500 (Internal Server Error)} if the protocolTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProtocolTemplate> partialUpdateProtocolTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProtocolTemplate protocolTemplate
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update ProtocolTemplate partially : {}, {}", id, protocolTemplate);
        if (protocolTemplate.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, protocolTemplate.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!protocolTemplateRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<ProtocolTemplate> result = protocolTemplateRepository
            .findById(protocolTemplate.getId())
            .map(existingProtocolTemplate -> {
                if (protocolTemplate.getName() != null) {
                    existingProtocolTemplate.setName(protocolTemplate.getName());
                }
                if (protocolTemplate.getDescription() != null) {
                    existingProtocolTemplate.setDescription(protocolTemplate.getDescription());
                }
                if (protocolTemplate.getTemplate() != null) {
                    existingProtocolTemplate.setTemplate(protocolTemplate.getTemplate());
                }
                if (protocolTemplate.getCreatedOrEdited() != null) {
                    existingProtocolTemplate.setCreatedOrEdited(protocolTemplate.getCreatedOrEdited());
                }

                return existingProtocolTemplate;
            })
            .map(protocolTemplateRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocolTemplate.getId().toString())
        );
    }

    /**
     * {@code GET  /protocol-templates} : get all the protocolTemplates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protocolTemplates in body.
     */
    @GetMapping("")
    public List<ProtocolTemplate> getAllProtocolTemplates() {
        log.debug("REST request to get all ProtocolTemplates");
        return protocolTemplateRepository.findAll();
    }

    /**
     * {@code GET  /protocol-templates/:id} : get the "id" protocolTemplate.
     *
     * @param id the id of the protocolTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocolTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProtocolTemplate> getProtocolTemplate(@PathVariable("id") Long id) {
        log.debug("REST request to get ProtocolTemplate : {}", id);
        Optional<ProtocolTemplate> protocolTemplate = protocolTemplateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(protocolTemplate);
    }

    /**
     * {@code DELETE  /protocol-templates/:id} : delete the "id" protocolTemplate.
     *
     * @param id the id of the protocolTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProtocolTemplate(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProtocolTemplate : {}", id);
        protocolTemplateRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
