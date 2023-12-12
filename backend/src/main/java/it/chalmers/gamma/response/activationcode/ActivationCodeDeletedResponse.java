package it.chalmers.gamma.response.activationcode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ActivationCodeDeletedResponse extends ResponseEntity<String> {
    public ActivationCodeDeletedResponse() {
        super("ACTIVATION_CODE_DELETED", HttpStatus.ACCEPTED);
    }
}
