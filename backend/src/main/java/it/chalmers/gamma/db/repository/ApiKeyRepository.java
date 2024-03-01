package it.chalmers.gamma.db.repository;

import it.chalmers.gamma.db.entity.ApiKey;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {
    boolean existsByKey(String apiKey);

    Optional<ApiKey> findByName(String apiKey);
}
