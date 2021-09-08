package it.chalmers.gamma.adapter.secondary.jpa.text;

import it.chalmers.gamma.domain.TextId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends JpaRepository<TextEntity, TextId> {
}
