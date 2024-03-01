package it.chalmers.gamma.response.authority;

import it.chalmers.gamma.response.CustomResponseStatusException;
import org.springframework.http.HttpStatus;

public class AuthorityLevelDoesNotExistException extends CustomResponseStatusException {
    public AuthorityLevelDoesNotExistException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "AUTHORITY_LEVEL_NOT_FOUND");
    }
}
