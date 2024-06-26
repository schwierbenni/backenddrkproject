package com.javabackend.backend.repository;

import com.javabackend.backend.domain.TemplateOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TemplateOrganization entity.
 */
@Repository
public interface TemplateOrganizationRepository extends JpaRepository<TemplateOrganization, Long> {}
