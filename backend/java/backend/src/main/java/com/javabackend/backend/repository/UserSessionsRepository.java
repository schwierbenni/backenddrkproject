package com.javabackend.backend.repository;

import com.javabackend.backend.domain.UserSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSessions entity.
 */
@Repository
public interface UserSessionsRepository extends JpaRepository<UserSessions, Long> {}
