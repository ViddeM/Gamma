package it.chalmers.gamma.bootstrap;

import it.chalmers.gamma.internal.apikey.service.ApiKeyService;
import it.chalmers.gamma.internal.authority.level.service.AuthorityLevelService;
import it.chalmers.gamma.internal.authority.post.service.AuthorityPostService;
import it.chalmers.gamma.internal.client.service.ClientFinder;
import it.chalmers.gamma.internal.group.service.GroupFinder;
import it.chalmers.gamma.internal.group.service.GroupService;
import it.chalmers.gamma.internal.post.service.PostFinder;
import it.chalmers.gamma.internal.supergroup.service.SuperGroupFinder;
import it.chalmers.gamma.internal.supergroup.service.SuperGroupService;
import it.chalmers.gamma.internal.client.service.ClientService;
import it.chalmers.gamma.internal.supergroup.type.service.SuperGroupTypeService;
import it.chalmers.gamma.internal.user.service.UserCreationService;
import it.chalmers.gamma.internal.user.service.UserFinder;
import it.chalmers.gamma.internal.user.service.UserService;
import it.chalmers.gamma.internal.membership.service.MembershipService;
import it.chalmers.gamma.internal.post.service.PostService;

import it.chalmers.gamma.internal.user.gdpr.service.UserGDPRTrainingService;
import org.springframework.stereotype.Component;

@Component()
public final class BootstrapServiceHelper {

    private final UserCreationService userCreationService;
    private final UserFinder userFinder;
    private final UserService userService;
    private final GroupService groupService;
    private final AuthorityLevelService authorityLevelService;
    private final PostService postService;
    private final MembershipService membershipService;
    private final AuthorityPostService authorityPostService;
    private final ClientService clientService;
    private final ApiKeyService apiKeyService;
    private final SuperGroupService superGroupService;
    private final PostFinder postFinder;
    private final GroupFinder groupFinder;
    private final SuperGroupFinder superGroupFinder;
    private final ClientFinder clientFinder;
    private final UserGDPRTrainingService userGDPRTrainingService;
    private final SuperGroupTypeService superGroupTypeService;

    public BootstrapServiceHelper(UserCreationService userCreationService,
                                  UserFinder userFinder,
                                  UserService userService,
                                  GroupService groupService,
                                  AuthorityLevelService authorityLevelService,
                                  PostService postService,
                                  MembershipService membershipService,
                                  AuthorityPostService authorityPostService,
                                  ClientService clientService,
                                  ApiKeyService apiKeyService,
                                  SuperGroupService superGroupService, PostFinder postFinder, GroupFinder groupFinder, SuperGroupFinder superGroupFinder, ClientFinder clientFinder, UserGDPRTrainingService userGDPRTrainingService, SuperGroupTypeService superGroupTypeService) {
        this.userCreationService = userCreationService;
        this.userFinder = userFinder;
        this.userService = userService;
        this.groupService = groupService;
        this.authorityLevelService = authorityLevelService;
        this.postService = postService;
        this.membershipService = membershipService;
        this.authorityPostService = authorityPostService;
        this.clientService = clientService;
        this.apiKeyService = apiKeyService;
        this.superGroupService = superGroupService;
        this.postFinder = postFinder;
        this.groupFinder = groupFinder;
        this.superGroupFinder = superGroupFinder;
        this.clientFinder = clientFinder;
        this.userGDPRTrainingService = userGDPRTrainingService;
        this.superGroupTypeService = superGroupTypeService;
    }

    public UserGDPRTrainingService getUserGDPRService() {
        return userGDPRTrainingService;
    }

    public SuperGroupFinder getSuperGroupFinder() {
        return superGroupFinder;
    }

    public GroupFinder getGroupFinder() {
        return groupFinder;
    }

    public UserService getUserService() {
        return this.userService;
    }

    public GroupService getGroupService() {
        return this.groupService;
    }

    public AuthorityLevelService getAuthorityLevelService() {
        return this.authorityLevelService;
    }

    public PostService getPostService() {
        return this.postService;
    }

    public MembershipService getMembershipService() {
        return this.membershipService;
    }

    public AuthorityPostService getAuthorityService() {
        return this.authorityPostService;
    }

    public ClientService getClientService() {
        return this.clientService;
    }

    public ApiKeyService getApiKeyService() {
        return this.apiKeyService;
    }

    public SuperGroupService getSuperGroupService() {
        return this.superGroupService;
    }

    public UserFinder getUserFinder() {
        return userFinder;
    }

    public UserCreationService getUserCreationService() {
        return userCreationService;
    }

    public PostFinder getPostFinder() {
        return postFinder;
    }

    public ClientFinder getClientFinder() {
        return clientFinder;
    }

    public SuperGroupTypeService getSuperGroupTypeService() {
        return superGroupTypeService;
    }
}
