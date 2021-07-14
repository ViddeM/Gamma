package it.chalmers.gamma.app.user;

import it.chalmers.gamma.adapter.secondary.jpa.user.UserActivationEntity;
import it.chalmers.gamma.adapter.secondary.jpa.user.UserActivationJpaRepository;
import it.chalmers.gamma.app.domain.UserActivation;
import it.chalmers.gamma.app.domain.UserActivationToken;
import it.chalmers.gamma.app.domain.Cid;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserActivationService {

    private final UserActivationJpaRepository repository;

    public UserActivationService(UserActivationJpaRepository repository) {
        this.repository = repository;
    }

    public UserActivation saveUserActivation(Cid cid) {
        // Delete if there was a code previously saved
        try {
            delete(cid);
        } catch (UserActivationNotFoundException ignored) {}

        return this.repository.save(new UserActivationEntity(cid, UserActivationToken.generate())).toDomain();
    }

    public void delete(Cid cid) throws UserActivationNotFoundException {
        try{
            this.repository.deleteById(cid);
        } catch(IllegalArgumentException e) {
            throw new UserActivationNotFoundException();
        }
    }

    public List<UserActivation> getAll() {
        return this.repository.findAll()
                .stream()
                .map(UserActivationEntity::toDomain)
                .collect(Collectors.toList());
    }

    public boolean codeMatchesCid(Cid cid, UserActivationToken token) {
        //TODO: check if useractivation is valid
        //    @Value("${password-expiration-time}")
        return this.repository.findUserActivationByCidAndToken(cid, token).isPresent();
    }

    public static class UserActivationNotFoundException extends Exception { }


}
