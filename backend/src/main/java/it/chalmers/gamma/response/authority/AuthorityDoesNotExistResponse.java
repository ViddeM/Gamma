package it.chalmers.gamma.response.authority;

import it.chalmers.gamma.response.CustomResponseStatusException;
import org.springframework.http.HttpStatus;

public class AuthorityDoesNotExistResponse extends CustomResponseStatusException {
    public AuthorityDoesNotExistResponse() {
        super(HttpStatus.NOT_FOUND,
                "AUTHORITY_DOES_NOT_EXIST");
    }
}
