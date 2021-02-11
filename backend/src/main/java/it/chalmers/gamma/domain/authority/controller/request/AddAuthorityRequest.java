package it.chalmers.gamma.domain.authority.controller.request;

import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;

public class AddAuthorityRequest {
    @NotNull(message = "POST_MUST_BE_PROVIDED")
    private UUID postId;
    @NotNull(message = "SUPER_GROUP_MUST_BE_PROVIDED")
    private UUID superGroupId;
    @NotNull(message = "AUTHORITY_MUST_BE_PROVIDED")
    private String authority;

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public UUID getSuperGroupId() {
        return superGroupId;
    }

    public void setSuperGroupId(UUID superGroupId) {
        this.superGroupId = superGroupId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "AuthorizationRequest{"
            + "post='" + this.postId + '\''
            + ", superGroup='" + this.superGroupId + '\''
            + ", authority'" + this.authority + '\''
            + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddAuthorityRequest that = (AddAuthorityRequest) o;
        return Objects.equals(this.postId, that.postId)
            && Objects.equals(this.superGroupId, that.superGroupId)
            && Objects.equals(this.authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.postId, this.superGroupId, this.authority);
    }
}
