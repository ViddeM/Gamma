package it.chalmers.gamma.adapter.secondary.jpa.group;

import java.util.List;

import it.chalmers.gamma.domain.group.GroupId;
import it.chalmers.gamma.domain.post.PostId;
import it.chalmers.gamma.domain.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<MembershipEntity, MembershipPK> {
    List<MembershipEntity> findAllById_PostId(PostId postId);
    List<MembershipEntity> findAllById_UserId(UserId userId);
    List<MembershipEntity> findAllById_GroupId(GroupId groupId);

    List<MembershipEntity> findAllById_GroupIdAndId_PostId(GroupId groupId, PostId postId);
}
