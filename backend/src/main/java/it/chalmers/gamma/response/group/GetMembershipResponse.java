package it.chalmers.gamma.response.group;

import it.chalmers.gamma.domain.dto.membership.RestrictedMembershipDTO;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetMembershipResponse {
    public final List<RestrictedMembershipDTO> members;

    public GetMembershipResponse(List<RestrictedMembershipDTO> members) {
        this.members = members;
    }

    public List<RestrictedMembershipDTO> getMembers() {
        return this.members;
    }

    public GetMembershipResponseObject toResponseObject() {
        return new GetMembershipResponseObject(this);
    }

    public static class GetMembershipResponseObject extends ResponseEntity<GetMembershipResponse> {
        public GetMembershipResponseObject(GetMembershipResponse body) {
            super(body, HttpStatus.OK);
        }
    }
}
