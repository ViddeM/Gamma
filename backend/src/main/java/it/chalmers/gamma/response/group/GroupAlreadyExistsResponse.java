package it.chalmers.gamma.response.group;

import it.chalmers.gamma.response.CustomResponseStatusException;
import org.springframework.http.HttpStatus;

public class GroupAlreadyExistsResponse extends CustomResponseStatusException {
    public GroupAlreadyExistsResponse() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "GROUP_ALREADY_EXISTS");
    }
}
