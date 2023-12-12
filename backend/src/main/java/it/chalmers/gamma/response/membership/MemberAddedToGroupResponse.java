package it.chalmers.gamma.response.membership;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MemberAddedToGroupResponse extends ResponseEntity<String> {
    public MemberAddedToGroupResponse() {
        super("USER_WAS_ADDED_TO_GROUP", HttpStatus.ACCEPTED);
    }
}
