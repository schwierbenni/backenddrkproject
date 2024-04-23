package com.javabackend.backend.repository;

import com.javabackend.backend.domain.ProtocolPdfFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProtocolPdfFile entity.
 */
@Repository
public interface ProtocolPdfFileRepository extends JpaRepository<ProtocolPdfFile, Long> {}
