package it.chalmers.gamma.bootstrap;

import it.chalmers.gamma.app.authoritylevel.AuthorityLevelFacade;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@DependsOn("groupBootstrap")
@Component
public class AuthorityBootstrap {

    private static final Logger LOGGER = Logger.getLogger(AuthorityBootstrap.class);

    private final MockData mockData;
    private final AuthorityLevelFacade authorityLevelFacade;
    private final boolean mocking;

    public AuthorityBootstrap(MockData mockData,
                              AuthorityLevelFacade authorityLevelFacade,
                              @Value("${application.mocking}") boolean mocking) {
        this.mockData = mockData;
        this.authorityLevelFacade = authorityLevelFacade;
        this.mocking = mocking;
    }

    @PostConstruct
    public void createAuthorities() {
        //!= 1 implies that admin isn't the only authority level
//        if (!this.mocking || this.authorityLevelService.getAll().size() != 1) {
//            return;
//        }
//
//        LOGGER.info("========== AUTHORITY BOOTSTRAP ==========");
//
//        this.mockData.users().forEach(mockUser -> {
//            if (mockUser.authorities() != null) {
//                mockUser.authorities().forEach(authorityLevelName -> {
//                    try {
//                        this.authorityLevelService.create(authorityLevelName);
//                    } catch (AuthorityLevelService.AuthorityLevelAlreadyExistsException ignored) { }
//
//                    try {
//                        this.authorityUserService.create(new AuthorityUserShallowDTO(mockUser.id(), authorityLevelName));
//                    } catch (AuthorityUserService.AuthorityUserNotFoundException ignored) { }
//                });
//            }
//        });
//
//        this.mockData.superGroups().forEach(mockSuperGroup -> {
//            if (mockSuperGroup.authorities() != null) {
//                mockSuperGroup.authorities().forEach(authorityLevelName -> {
//                    try {
//                        this.authorityLevelService.create(authorityLevelName);
//                    } catch (AuthorityLevelService.AuthorityLevelAlreadyExistsException ignored) {
//                    }
//
//                    try {
//                        this.authoritySuperGroupService.create(new AuthoritySuperGroupDTO(mockSuperGroup.id(), authorityLevelName));
//                    } catch (AuthoritySuperGroupService.AuthoritySuperGroupAlreadyExistsException ignored) {
//                    }
//                });
//            }
//        });
//
//        this.mockData.postAuthorities().forEach(mockPostAuthority -> {
//            try {
//                this.authorityLevelService.create(mockPostAuthority.name());
//            } catch (AuthorityLevelService.AuthorityLevelAlreadyExistsException ignored) { }
//
//            try {
//                this.authorityPostService.create(new AuthorityPostDTO(
//                        mockPostAuthority.superGroupId(),
//                        mockPostAuthority.postId(),
//                        mockPostAuthority.name())
//                );
//            } catch (AuthorityPostService.AuthorityPostNotFoundException ignored) { }
//        });

        LOGGER.info("========== AUTHORITY BOOTSTRAP ==========");
    }

}
