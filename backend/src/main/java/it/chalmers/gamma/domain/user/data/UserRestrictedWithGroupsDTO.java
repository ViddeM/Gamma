package it.chalmers.gamma.domain.user.data;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import it.chalmers.gamma.domain.group.data.GroupDTO;
import it.chalmers.gamma.domain.post.data.PostDTO;

import java.util.List;

public class UserRestrictedWithGroupsDTO {

    @JsonUnwrapped
    private final UserRestrictedDTO user;
    private final List<UserGroup> groups;

    public UserRestrictedWithGroupsDTO(UserRestrictedDTO user, List<UserGroup> groups) {
        this.user = user;
        this.groups = groups;
    }

    public UserRestrictedDTO getUser() {
        return user;
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    public static class UserGroup {
        private final PostDTO post;
        private final GroupDTO group;

        public UserGroup(PostDTO post, GroupDTO group) {
            this.post = post;
            this.group = group;
        }

        public PostDTO getPost() {
            return post;
        }

        public GroupDTO getGroup() {
            return group;
        }
    }
}
