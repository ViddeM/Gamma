package it.chalmers.gamma.group.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GroupCreatedResponse extends ResponseEntity<String> {

    public GroupCreatedResponse() {
        super("GROUP_CREATED", HttpStatus.ACCEPTED);
    }
}
