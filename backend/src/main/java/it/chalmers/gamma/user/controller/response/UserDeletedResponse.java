package it.chalmers.gamma.user.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserDeletedResponse extends ResponseEntity<String> {
    public UserDeletedResponse() {
        super("USER_DELETED", HttpStatus.OK);
    }
}
