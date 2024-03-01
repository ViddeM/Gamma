package it.chalmers.gamma.response.user;

import it.chalmers.gamma.response.CustomResponseStatusException;
import org.springframework.http.HttpStatus;

public class PasswordTooShortResponse extends CustomResponseStatusException {
    public PasswordTooShortResponse() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "TOO_SHORT_PASSWORD");
    }
}
