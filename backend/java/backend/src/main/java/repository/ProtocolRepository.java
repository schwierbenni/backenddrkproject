package repository;

import domain.Protocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Protocol entity.
 */
@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, Long> {}
