package repository;

import domain.UserSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSessions entity.
 */
@Repository
public interface UserSessionsRepository extends JpaRepository<UserSessions, Long> {}
