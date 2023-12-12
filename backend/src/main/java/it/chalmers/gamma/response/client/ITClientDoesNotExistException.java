package it.chalmers.gamma.response.client;

import it.chalmers.gamma.response.CustomResponseStatusException;
import org.springframework.http.HttpStatus;

public class ITClientDoesNotExistException extends CustomResponseStatusException {
    public ITClientDoesNotExistException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "NO_SUCH_CLIENT_EXISTS");
    }
}
