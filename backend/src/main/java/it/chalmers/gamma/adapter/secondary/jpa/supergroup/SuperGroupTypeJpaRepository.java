package it.chalmers.gamma.adapter.secondary.jpa.supergroup;

import it.chalmers.gamma.app.domain.supergroup.SuperGroupType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperGroupTypeJpaRepository extends JpaRepository<SuperGroupTypeEntity, SuperGroupType> {
}
