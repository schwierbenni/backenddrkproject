package com.javabackend.backend.repository;

import com.javabackend.backend.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserRole entity.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {}
