package it.chalmers.gamma.requests;

import it.chalmers.gamma.domain.user.UserId;

import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;

public class AddUserGroupRequest {

    @NotEmpty(message = "USER_MUST_BE_PROVIDED")
    private UserId userId;

    @NotEmpty(message = "POST_MUST_BE_PROVIDED")
    private UUID postId;
    private String unofficialName;

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getUnofficialName() {
        return this.unofficialName;
    }

    public void setUnofficialName(String unofficialName) {
        this.unofficialName = unofficialName;
    }

}
