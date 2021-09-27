package it.chalmers.gamma.adapter.secondary.jpa.user.password;

import it.chalmers.gamma.app.domain.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordResetJpaRepository extends JpaRepository<UserPasswordResetEntity, UserId> { }
