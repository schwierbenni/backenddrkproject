package rest;

import domain.ProtocolContent;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import repository.ProtocolContentRepository;
import util.HeaderUtil;
import util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link domain.ProtocolContent}.
 */
@RestController
@RequestMapping("/api/protocol-contents")
@Transactional
public class ProtocolContentResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolContentResource.class);

    private static final String ENTITY_NAME = "protocolContent";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ProtocolContentRepository protocolContentRepository;

    public ProtocolContentResource(ProtocolContentRepository protocolContentRepository) {
        this.protocolContentRepository = protocolContentRepository;
    }

    /**
     * {@code POST  /protocol-contents} : Create a new protocolContent.
     *
     * @param protocolContent the protocolContent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protocolContent, or with status {@code 400 (Bad Request)} if the protocolContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProtocolContent> createProtocolContent(@RequestBody ProtocolContent protocolContent) throws URISyntaxException, BadRequestException {
        log.debug("REST request to save ProtocolContent : {}", protocolContent);
        if (protocolContent.getId() != null) {
            throw new BadRequestException("A new protocolContent cannot already have an ID");
        }
        ProtocolContent result = protocolContentRepository.save(protocolContent);
        return ResponseEntity
                .created(new URI("/api/protocol-contents/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /protocol-contents/:id} : Updates an existing protocolContent.
     *
     * @param id              the id of the protocolContent to save.
     * @param protocolContent the protocolContent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolContent,
     * or with status {@code 400 (Bad Request)} if the protocolContent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protocolContent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProtocolContent> updateProtocolContent(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody ProtocolContent protocolContent
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update ProtocolContent : {}, {}", id, protocolContent);
        if (protocolContent.getId() == null) {
            throw new BadRequestException("Invalid ID");
        }
        if (!Objects.equals(id, protocolContent.getId())) {
            throw new BadRequestException("Invalid ID");
        }

        if (!protocolContentRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        ProtocolContent result = protocolContentRepository.save(protocolContent);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocolContent.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /protocol-contents/:id} : Partial updates given fields of an existing protocolContent, field will ignore if it is null
     *
     * @param id              the id of the protocolContent to save.
     * @param protocolContent the protocolContent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolContent,
     * or with status {@code 400 (Bad Request)} if the protocolContent is not valid,
     * or with status {@code 404 (Not Found)} if the protocolContent is not found,
     * or with status {@code 500 (Internal Server Error)} if the protocolContent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<ProtocolContent> partialUpdateProtocolContent(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody ProtocolContent protocolContent
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update ProtocolContent partially : {}, {}", id, protocolContent);
        if (protocolContent.getId() == null) {
            throw new BadRequestException("Invalid ID");
        }
        if (!Objects.equals(id, protocolContent.getId())) {
            throw new BadRequestException("Invalid ID");
        }

        if (!protocolContentRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<ProtocolContent> result = protocolContentRepository
                .findById(protocolContent.getId())
                .map(existingProtocolContent -> {
                    if (protocolContent.getContent() != null) {
                        existingProtocolContent.setContent(protocolContent.getContent());
                    }

                    return existingProtocolContent;
                })
                .map(protocolContentRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocolContent.getId().toString())
        );
    }

    /**
     * {@code GET  /protocol-contents} : get all the protocolContents.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protocolContents in body.
     */
    @GetMapping("")
    public List<ProtocolContent> getAllProtocolContents(@RequestParam(name = "filter", required = false) String filter) {
        if ("protocol-is-null".equals(filter)) {
            log.debug("REST request to get all ProtocolContents where protocol is null");
            return StreamSupport
                    .stream(protocolContentRepository.findAll().spliterator(), false)
                    .filter(protocolContent -> protocolContent.getProtocol() == null)
                    .toList();
        }
        log.debug("REST request to get all ProtocolContents");
        return protocolContentRepository.findAll();
    }

    /**
     * {@code GET  /protocol-contents/:id} : get the "id" protocolContent.
     *
     * @param id the id of the protocolContent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocolContent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProtocolContent> getProtocolContent(@PathVariable("id") Long id) {
        log.debug("REST request to get ProtocolContent : {}", id);
        Optional<ProtocolContent> protocolContent = protocolContentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(protocolContent);
    }

    /**
     * {@code DELETE  /protocol-contents/:id} : delete the "id" protocolContent.
     *
     * @param id the id of the protocolContent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProtocolContent(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProtocolContent : {}", id);
        protocolContentRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
