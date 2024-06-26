package com.javabackend.backend.repository;

import com.javabackend.backend.domain.DrkUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DrkUser entity.
 */
@Repository
public interface DrkUserRepository extends JpaRepository<DrkUser, Long> {}
