package com.javabackend.backend.repository;


import com.javabackend.backend.domain.AdditionalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdditionalUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdditionalUserRepository extends JpaRepository<AdditionalUser, Long> {}
