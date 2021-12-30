package it.chalmers.gamma.adapter.secondary.jpa.group;

import it.chalmers.gamma.adapter.secondary.jpa.supergroup.SuperGroupEntityConverter;
import it.chalmers.gamma.adapter.secondary.jpa.supergroup.SuperGroupJpaRepository;
import it.chalmers.gamma.adapter.secondary.jpa.user.UserEntityConverter;
import it.chalmers.gamma.adapter.secondary.jpa.user.UserJpaRepository;
import it.chalmers.gamma.app.group.domain.GroupId;
import it.chalmers.gamma.app.image.domain.ImageUri;
import it.chalmers.gamma.app.common.PrettyName;
import it.chalmers.gamma.app.group.domain.Group;
import it.chalmers.gamma.app.group.domain.GroupMember;
import it.chalmers.gamma.app.group.domain.UnofficialPostName;
import it.chalmers.gamma.app.user.domain.Name;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupEntityConverter {

    private final UserEntityConverter userEntityConverter;
    private final UserJpaRepository userJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final PostEntityConverter postEntityConverter;
    private final GroupJpaRepository groupJpaRepository;
    private final SuperGroupJpaRepository superGroupJpaRepository;
    private final SuperGroupEntityConverter superGroupEntityConverter;

    public GroupEntityConverter(UserEntityConverter userEntityConverter,
                                UserJpaRepository userJpaRepository,
                                PostJpaRepository postJpaRepository,
                                PostEntityConverter postEntityConverter,
                                GroupJpaRepository groupJpaRepository,
                                SuperGroupJpaRepository superGroupJpaRepository,
                                SuperGroupEntityConverter superGroupEntityConverter) {
        this.userEntityConverter = userEntityConverter;
        this.userJpaRepository = userJpaRepository;
        this.postJpaRepository = postJpaRepository;
        this.postEntityConverter = postEntityConverter;
        this.groupJpaRepository = groupJpaRepository;
        this.superGroupJpaRepository = superGroupJpaRepository;
        this.superGroupEntityConverter = superGroupEntityConverter;
    }

    public GroupEntity toEntity(Group group) {
        Optional<GroupEntity> maybeEntity = this.groupJpaRepository.findById(group.id().value());
        GroupEntity entity;

        if (maybeEntity.isPresent()) {
            entity = maybeEntity.get();
            entity.increaseVersion(group.version());
        } else {
            entity = new GroupEntity();
        }

        entity.id = group.id().getValue();
        entity.name = group.name().value();
        entity.prettyName = group.prettyName().value();
        entity.superGroup = superGroupJpaRepository.getById(group.superGroup().id().value());

        if (entity.members == null) {
            entity.members = new ArrayList<>();
        }

        entity.members.clear();
        entity.members.addAll(group.groupMembers()
                .stream()
                .map(groupMember -> new MembershipEntity(
                        new MembershipPK(
                                this.postJpaRepository.getById(groupMember.post().id().value()),
                                entity,
                                this.userJpaRepository.getById(groupMember.user().id().value())),
                        groupMember.unofficialPostName().value()
                )).toList());

        if (entity.groupImages == null) {
            entity.groupImages = new GroupImagesEntity();
        }

        entity.groupImages.group = entity;
        entity.groupImages.groupId = entity.id;
        entity.groupImages.avatarUri = group.avatarUri().map(ImageUri::value).orElse(null);
        entity.groupImages.bannerUri = group.bannerUri().map(ImageUri::value).orElse(null);

        return entity;
    }

    public Group toDomain(GroupEntity entity) {
        //TODO: add imageuri and members
        List<GroupMember> members = entity.getMembers()
                .stream()
                .map(membershipEntity -> new GroupMember(
                        this.postEntityConverter.toDomain(membershipEntity.getId().getPost()),
                        new UnofficialPostName(membershipEntity.getUnofficialPostName()),
                        this.userEntityConverter.toDomain(membershipEntity.getId().getUser())
                ))
                .toList();

        Optional<ImageUri> avatarUri = Optional.empty();
        Optional<ImageUri> bannerUri = Optional.empty();

        if (entity.groupImages != null) {
            avatarUri = Optional.ofNullable(entity.groupImages.avatarUri).map(ImageUri::new);
            bannerUri = Optional.ofNullable(entity.groupImages.bannerUri).map(ImageUri::new);
        }

        return new Group(
                new GroupId(entity.getId()),
                entity.getVersion(),
                new Name(entity.name),
                new PrettyName(entity.prettyName),
                this.superGroupEntityConverter.toDomain(entity.superGroup),
                members,
                avatarUri,
                bannerUri
        );
    }

}
