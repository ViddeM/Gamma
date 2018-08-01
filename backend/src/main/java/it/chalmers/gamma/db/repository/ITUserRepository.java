package it.chalmers.gamma.db.repository;

import it.chalmers.gamma.db.entity.ITUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ITUserRepository extends JpaRepository<ITUser, UUID> {

    ITUser findByCid(String cid);
    boolean existsByCid(String cid);
}
