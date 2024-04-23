package com.javabackend.backend.repository;

import com.javabackend.backend.domain.ProtocolContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProtocolContent entity.
 */
@Repository
public interface ProtocolContentRepository extends JpaRepository<ProtocolContent, Long> {}
