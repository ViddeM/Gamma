package it.chalmers.gamma.util.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import it.chalmers.gamma.domain.user.service.UserRestrictedDTO;

import java.util.List;

public class UserWithGroups {

    @JsonUnwrapped
    public final UserRestrictedDTO user;
    public final List<GroupPost> groups;

    public UserWithGroups(UserRestrictedDTO user, List<GroupPost> groups) {
        this.user = user;
        this.groups = groups;
    }
}

