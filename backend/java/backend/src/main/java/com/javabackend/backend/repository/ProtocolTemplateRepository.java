package com.javabackend.backend.repository;

import com.javabackend.backend.domain.ProtocolTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProtocolTemplate entity.
 */
@Repository
public interface ProtocolTemplateRepository extends JpaRepository<ProtocolTemplate, Long> {}
