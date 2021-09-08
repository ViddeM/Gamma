package it.chalmers.gamma.app;

import it.chalmers.gamma.app.authority.AuthorityLevelRepository;
import it.chalmers.gamma.domain.authoritylevel.AuthorityLevel;
import it.chalmers.gamma.domain.authoritylevel.AuthorityLevelName;
import it.chalmers.gamma.domain.post.PostId;
import it.chalmers.gamma.domain.supergroup.SuperGroupId;
import it.chalmers.gamma.domain.user.UserId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityLevelFacade extends Facade {

    private final AuthorityLevelRepository authorityLevelRepository;

    public AuthorityLevelFacade(AccessGuard accessGuard, AuthorityLevelRepository authorityLevelRepository) {
        super(accessGuard);
        this.authorityLevelRepository = authorityLevelRepository;
    }

    public void create(AuthorityLevelName authorityLevelName) {
        accessGuard.requireIsAdmin();
        this.authorityLevelRepository.create(authorityLevelName);
    }

    public void delete(AuthorityLevelName authorityLevelName) {
        accessGuard.requireIsAdmin();
        this.authorityLevelRepository.delete(authorityLevelName);
    }

    public Optional<AuthorityLevel> get(AuthorityLevelName authorityLevelName) {
        return this.authorityLevelRepository.get(authorityLevelName);
    }

    public List<AuthorityLevel> getAll() {
        accessGuard.requireIsAdmin();
        return this.authorityLevelRepository.getAll();
    }


    public void addToAuthorityLevel(AuthorityLevelName authorityLevelName, SuperGroupId superGroupId) {

    }

    public void addToAuthorityLevel(AuthorityLevelName authorityLevelName, SuperGroupId superGroupId, PostId postId) {

    }

    public void addToAuthorityLevel(AuthorityLevelName authorityLevelName, UserId userId) {

    }

    public void removeFromAuthorityLevel(AuthorityLevelName name, SuperGroupId superGroupId) {

    }

    public void removeFromAuthorityLevel(AuthorityLevelName name, SuperGroupId superGroupId, PostId postId) {

    }

    public void removeFromAuthorityLevel(AuthorityLevelName name, UserId userId) {

    }

}
