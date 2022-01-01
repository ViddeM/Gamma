package it.chalmers.gamma.app.authentication;

import it.chalmers.gamma.app.apikey.domain.ApiKeyType;
import it.chalmers.gamma.app.authoritylevel.domain.AuthorityLevelName;
import it.chalmers.gamma.app.authoritylevel.domain.AuthorityLevelRepository;
import it.chalmers.gamma.app.client.domain.Client;
import it.chalmers.gamma.app.group.domain.Group;
import it.chalmers.gamma.app.password.PasswordService;
import it.chalmers.gamma.app.user.domain.UnencryptedPassword;
import it.chalmers.gamma.app.user.domain.User;
import it.chalmers.gamma.app.user.domain.UserAuthority;
import it.chalmers.gamma.app.user.domain.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessGuard {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessGuard.class);
    private static final AuthorityLevelName adminAuthority = new AuthorityLevelName("admin");

    private final AuthenticatedService authenticatedService;
    private final PasswordService passwordService;
    private final AuthorityLevelRepository authorityLevelRepository;

    public AccessGuard(AuthenticatedService authenticatedService,
                       PasswordService passwordService,
                       AuthorityLevelRepository authorityLevelRepository) {
        this.authenticatedService = authenticatedService;
        this.passwordService = passwordService;
        this.authorityLevelRepository = authorityLevelRepository;
    }

    public void require(AccessChecker check) {
        if (!validate(check)) {
            throw new AccessDeniedException();
        }
    }

    public void requireEither(AccessChecker... checks) {
        for (AccessChecker check : checks) {
            if (validate(check)) {
                return;
            }
        }

        //None of the check went through thus access denied
        throw new AccessDeniedException();
    }

    public void requireAll(AccessChecker... checks) {
        for (AccessChecker check : checks) {
            if (!validate(check)) {
                //If any of the checks fails, then access denied
                throw new AccessDeniedException();
            }
        }
    }

    private boolean validate(AccessChecker check) {
        return check.validate(authenticatedService, passwordService, authorityLevelRepository);
    }

    public static AccessChecker isAdmin() {
        return (authenticatedService, passwordService, authorityLevelRepository) -> {
            if (authenticatedService.getAuthenticated() instanceof InternalUserAuthenticated userAuthenticated) {
                User user = userAuthenticated.get();
                List<AuthorityLevelName> authorities = authorityLevelRepository.getByUser(user.id())
                        .stream()
                        .map(UserAuthority::authorityLevelName)
                        .toList();
                return authorities.contains(adminAuthority);
            }

            return false;
        };
    }

    public static AccessChecker passwordCheck(String password) {
        return (authenticatedService, passwordService, authorityLevelRepository) -> {
            if (authenticatedService.getAuthenticated() instanceof InternalUserAuthenticated userAuthenticated) {
                User user = userAuthenticated.get();
                return passwordService.matches(new UnencryptedPassword(password), user.extended().password());
            }

            return false;
        };
    }

    public static AccessChecker isApi(ApiKeyType apiKeyType) {
        return (authenticatedService, passwordService, authorityLevelRepository) -> {
            if (authenticatedService.getAuthenticated() instanceof ApiAuthenticated apiAuthenticated) {
                return apiAuthenticated.get().keyType() == apiKeyType;
            }

            return false;
        };
    }

    public static AccessChecker isClientApi() {
        return (authenticatedService, passwordService, authorityLevelRepository) -> {
            if (authenticatedService.getAuthenticated() instanceof ApiAuthenticated apiAuthenticated) {
                return apiAuthenticated.get().keyType() == ApiKeyType.CLIENT;
            }

            return false;
        };
    }

    public static AccessChecker isSignedInUserMemberOfGroup(Group group) {
        return (authenticatedService, passwordService, authorityLevelRepository) -> {
            if (authenticatedService.getAuthenticated() instanceof InternalUserAuthenticated internalUserAuthenticated) {
                User user = internalUserAuthenticated.get();
                return group.groupMembers().stream().anyMatch(groupMember -> groupMember.user().equals(user));
            }

            return false;
        };
    }


    public static AccessChecker isSignedIn() {
        return (authenticatedService, passwordService, authorityLevelRepository) -> authenticatedService.getAuthenticated() instanceof InternalUserAuthenticated;
    }

    public static AccessChecker isNotSignedIn() {
        return (authenticatedService, passwordService, authorityLevelRepository) ->
                authenticatedService.getAuthenticated() instanceof Unauthenticated;
    }

    public static AccessChecker userHasAcceptedClient(UserId id) {
        return (authenticatedService, passwordService, authorityLevelRepository) -> {
            if (authenticatedService.getAuthenticated() instanceof ApiAuthenticated apiAuthenticated) {
                if (apiAuthenticated.getClient().isPresent()) {
                    Client client = apiAuthenticated.getClient().get();
                    return client.approvedUsers().stream().anyMatch(user -> user.id().equals(id));
                }
            }

            return false;
        };
    }

    /**
     * Such as Bootstrap
     */
    public static AccessChecker isLocalRunner() {
        return (authenticatedService, passwordService, authorityLevelRepository) ->
                authenticatedService.getAuthenticated() instanceof LocalRunnerAuthenticated;
    }

    public interface AccessChecker {
        boolean validate(AuthenticatedService authenticatedService,
                         PasswordService passwordService,
                         AuthorityLevelRepository authorityLevelRepository);
    }

    public static class AccessDeniedException extends RuntimeException { }

}

