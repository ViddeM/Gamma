package it.chalmers.gamma.app.user.domain;

import it.chalmers.gamma.app.common.Email;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);
    void delete(UserId userId) throws UserNotFoundException;

    List<User> getAll();
    Optional<User> get(UserId userId);
    Optional<User> get(Cid cid);
    Optional<User> get(Email email);

    void acceptUserAgreement(UserId userId) throws UserNotFoundException;

    class UserNotFoundException extends Exception { }

}
