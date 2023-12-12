package it.chalmers.gamma.response.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import it.chalmers.gamma.domain.dto.group.FKITGroupDTO;

import it.chalmers.gamma.domain.dto.membership.NoAccountMembershipDTO;
import it.chalmers.gamma.domain.dto.membership.RestrictedMembershipDTO;
import it.chalmers.gamma.domain.dto.website.WebsiteUrlDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetFKITGroupResponse {
    @JsonUnwrapped
    private final FKITGroupDTO group;
    private final List<RestrictedMembershipDTO> groupMembers;
    private final List<NoAccountMembershipDTO> noAccountMembers;
    private final List<WebsiteUrlDTO> websites;

    public GetFKITGroupResponse(FKITGroupDTO group,
                                List<RestrictedMembershipDTO> groupMembers,
                                List<NoAccountMembershipDTO> noAccountMembers,
                                List<WebsiteUrlDTO> websites) {
        this.group = group;
        this.groupMembers = groupMembers;
        this.noAccountMembers = noAccountMembers;
        this.websites = websites;
    }

    public GetFKITGroupResponse(FKITGroupDTO group, List<RestrictedMembershipDTO> groupMembers) {
        this(group, groupMembers, null, null);
    }

    public FKITGroupDTO getGroup() {
        return this.group;
    }

    public List<RestrictedMembershipDTO> getGroupMembers() {
        return this.groupMembers;
    }

    public List<NoAccountMembershipDTO> getNoAccountMembers() {
        return this.noAccountMembers;
    }

    public List<WebsiteUrlDTO> getWebsites() {
        return this.websites;
    }

    public GetFKITGroupResponseObject toResponseObject() {
        return new GetFKITGroupResponseObject(this);
    }

    public static class GetFKITGroupResponseObject extends ResponseEntity<GetFKITGroupResponse> {
        public GetFKITGroupResponseObject(GetFKITGroupResponse body) {
            super(body, HttpStatus.OK);
        }
    }
}
