package rest;

import domain.ProtocolPdfFile;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import repository.ProtocolPdfFileRepository;
import util.HeaderUtil;
import util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link domain.ProtocolPdfFile}.
 */
@RestController
@RequestMapping("/api/protocol-pdf-files")
@Transactional
public class ProtocolPdfFileResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolPdfFileResource.class);

    private static final String ENTITY_NAME = "protocolPdfFile";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ProtocolPdfFileRepository protocolPdfFileRepository;

    public ProtocolPdfFileResource(ProtocolPdfFileRepository protocolPdfFileRepository) {
        this.protocolPdfFileRepository = protocolPdfFileRepository;
    }

    /**
     * {@code POST  /protocol-pdf-files} : Create a new protocolPdfFile.
     *
     * @param protocolPdfFile the protocolPdfFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protocolPdfFile, or with status {@code 400 (Bad Request)} if the protocolPdfFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProtocolPdfFile> createProtocolPdfFile(@RequestBody ProtocolPdfFile protocolPdfFile) throws URISyntaxException, BadRequestException {
        log.debug("REST request to save ProtocolPdfFile : {}", protocolPdfFile);
        if (protocolPdfFile.getId() != null) {
            throw new BadRequestException("A new protocolPdfFile cannot already have an ID");
        }
        ProtocolPdfFile result = protocolPdfFileRepository.save(protocolPdfFile);
        return ResponseEntity
            .created(new URI("/api/protocol-pdf-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /protocol-pdf-files/:id} : Updates an existing protocolPdfFile.
     *
     * @param id the id of the protocolPdfFile to save.
     * @param protocolPdfFile the protocolPdfFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolPdfFile,
     * or with status {@code 400 (Bad Request)} if the protocolPdfFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protocolPdfFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProtocolPdfFile> updateProtocolPdfFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProtocolPdfFile protocolPdfFile
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update ProtocolPdfFile : {}, {}", id, protocolPdfFile);
        if (protocolPdfFile.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, protocolPdfFile.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!protocolPdfFileRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        ProtocolPdfFile result = protocolPdfFileRepository.save(protocolPdfFile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocolPdfFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /protocol-pdf-files/:id} : Partial updates given fields of an existing protocolPdfFile, field will ignore if it is null
     *
     * @param id the id of the protocolPdfFile to save.
     * @param protocolPdfFile the protocolPdfFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolPdfFile,
     * or with status {@code 400 (Bad Request)} if the protocolPdfFile is not valid,
     * or with status {@code 404 (Not Found)} if the protocolPdfFile is not found,
     * or with status {@code 500 (Internal Server Error)} if the protocolPdfFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProtocolPdfFile> partialUpdateProtocolPdfFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProtocolPdfFile protocolPdfFile
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update ProtocolPdfFile partially : {}, {}", id, protocolPdfFile);
        if (protocolPdfFile.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, protocolPdfFile.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!protocolPdfFileRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<ProtocolPdfFile> result = protocolPdfFileRepository
            .findById(protocolPdfFile.getId())
            .map(existingProtocolPdfFile -> {
                if (protocolPdfFile.getContent() != null) {
                    existingProtocolPdfFile.setContent(protocolPdfFile.getContent());
                }
                if (protocolPdfFile.getMimeType() != null) {
                    existingProtocolPdfFile.setMimeType(protocolPdfFile.getMimeType());
                }

                return existingProtocolPdfFile;
            })
            .map(protocolPdfFileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocolPdfFile.getId().toString())
        );
    }

    /**
     * {@code GET  /protocol-pdf-files} : get all the protocolPdfFiles.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protocolPdfFiles in body.
     */
    @GetMapping("")
    public List<ProtocolPdfFile> getAllProtocolPdfFiles(@RequestParam(name = "filter", required = false) String filter) {
        if ("protocol-is-null".equals(filter)) {
            log.debug("REST request to get all ProtocolPdfFiles where protocol is null");
            return StreamSupport
                .stream(protocolPdfFileRepository.findAll().spliterator(), false)
                .filter(protocolPdfFile -> protocolPdfFile.getProtocol() == null)
                .toList();
        }
        log.debug("REST request to get all ProtocolPdfFiles");
        return protocolPdfFileRepository.findAll();
    }

    /**
     * {@code GET  /protocol-pdf-files/:id} : get the "id" protocolPdfFile.
     *
     * @param id the id of the protocolPdfFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocolPdfFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProtocolPdfFile> getProtocolPdfFile(@PathVariable("id") Long id) {
        log.debug("REST request to get ProtocolPdfFile : {}", id);
        Optional<ProtocolPdfFile> protocolPdfFile = protocolPdfFileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(protocolPdfFile);
    }

    /**
     * {@code DELETE  /protocol-pdf-files/:id} : delete the "id" protocolPdfFile.
     *
     * @param id the id of the protocolPdfFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProtocolPdfFile(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProtocolPdfFile : {}", id);
        protocolPdfFileRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
