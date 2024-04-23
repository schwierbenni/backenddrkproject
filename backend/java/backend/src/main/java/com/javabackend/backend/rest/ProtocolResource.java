package com.javabackend.backend.rest;


import com.javabackend.backend.util.HeaderUtil;
import com.javabackend.backend.util.PaginationUtil;
import com.javabackend.backend.util.ResponseUtil;
import com.javabackend.backend.domain.Protocol;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.javabackend.backend.repository.ProtocolRepository;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Protocol}.
 */
@RequestMapping("/api/protocols")
@Transactional
public class ProtocolResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolResource.class);

    private static final String ENTITY_NAME = "protocol";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ProtocolRepository protocolRepository;

    public ProtocolResource(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    /**
     * {@code POST  /protocols} : Create a new protocol.
     *
     * @param protocol the protocol to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protocol, or with status {@code 400 (Bad Request)} if the protocol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Protocol> createProtocol(@RequestBody Protocol protocol) throws URISyntaxException, BadRequestException {
        log.debug("REST request to save Protocol : {}", protocol);
        if (protocol.getId() != null) {
            throw new BadRequestException("A new protocol cannot already have an ID");
        }
        Protocol result = protocolRepository.save(protocol);
        return ResponseEntity
            .created(new URI("/api/protocols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /protocols/:id} : Updates an existing protocol.
     *
     * @param id the id of the protocol to save.
     * @param protocol the protocol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocol,
     * or with status {@code 400 (Bad Request)} if the protocol is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protocol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Protocol> updateProtocol(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Protocol protocol
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to update Protocol : {}, {}", id, protocol);
        if (protocol.getId() == null) {
            throw new BadRequestException("Invalid ID");
        }
        if (!Objects.equals(id, protocol.getId())) {
            throw new BadRequestException("Invalid ID");
        }

        if (!protocolRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Protocol result = protocolRepository.save(protocol);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocol.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /protocols/:id} : Partial updates given fields of an existing protocol, field will ignore if it is null
     *
     * @param id the id of the protocol to save.
     * @param protocol the protocol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocol,
     * or with status {@code 400 (Bad Request)} if the protocol is not valid,
     * or with status {@code 404 (Not Found)} if the protocol is not found,
     * or with status {@code 500 (Internal Server Error)} if the protocol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Protocol> partialUpdateProtocol(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Protocol protocol
    ) throws URISyntaxException, BadRequestException {
        log.debug("REST request to partial update Protocol partially : {}, {}", id, protocol);
        if (protocol.getId() == null) {
            throw new BadRequestException("Invalid ID");
        }
        if (!Objects.equals(id, protocol.getId())) {
            throw new BadRequestException("Invalid ID");
        }

        if (!protocolRepository.existsById(id)) {
            throw new BadRequestException("Entity not found");
        }

        Optional<Protocol> result = protocolRepository
            .findById(protocol.getId())
            .map(existingProtocol -> {
                if (protocol.getIsDraft() != null) {
                    existingProtocol.setIsDraft(protocol.getIsDraft());
                }
                if (protocol.getReviewComment() != null) {
                    existingProtocol.setReviewComment(protocol.getReviewComment());
                }
                if (protocol.getIsClosed() != null) {
                    existingProtocol.setIsClosed(protocol.getIsClosed());
                }
                if (protocol.getClosedAt() != null) {
                    existingProtocol.setClosedAt(protocol.getClosedAt());
                }
                if (protocol.getCreatedOrEdited() != null) {
                    existingProtocol.setCreatedOrEdited(protocol.getCreatedOrEdited());
                }

                return existingProtocol;
            })
            .map(protocolRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocol.getId().toString())
        );
    }

    /**
     * {@code GET  /protocols} : get all the protocols.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protocols in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Protocol>> getAllProtocols(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Protocols");
        Page<Protocol> page = protocolRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /protocols/:id} : get the "id" protocol.
     *
     * @param id the id of the protocol to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocol, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Protocol> getProtocol(@PathVariable("id") Long id) {
        log.debug("REST request to get Protocol : {}", id);
        Optional<Protocol> protocol = protocolRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(protocol);
    }

    /**
     * {@code DELETE  /protocols/:id} : delete the "id" protocol.
     *
     * @param id the id of the protocol to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProtocol(@PathVariable("id") Long id) {
        log.debug("REST request to delete Protocol : {}", id);
        protocolRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
