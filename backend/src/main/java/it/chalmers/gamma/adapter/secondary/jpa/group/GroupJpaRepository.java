package it.chalmers.gamma.adapter.secondary.jpa.group;

import java.util.List;
import java.util.UUID;

import it.chalmers.gamma.app.domain.supergroup.SuperGroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJpaRepository extends JpaRepository<GroupEntity, UUID> {
    List<GroupEntity> findAllBySuperGroupId(SuperGroupId id);
}
