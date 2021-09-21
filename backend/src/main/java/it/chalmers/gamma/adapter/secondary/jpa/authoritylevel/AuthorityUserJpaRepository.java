package it.chalmers.gamma.adapter.secondary.jpa.authoritylevel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityUserJpaRepository extends JpaRepository<AuthorityUserEntity, AuthorityUserPK> {

}
