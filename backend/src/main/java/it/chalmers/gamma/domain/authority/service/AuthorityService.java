package it.chalmers.gamma.domain.authority.service;

import it.chalmers.gamma.domain.authority.data.Authority;
import it.chalmers.gamma.domain.authority.data.AuthorityDTO;
import it.chalmers.gamma.domain.authority.data.AuthorityPK;
import it.chalmers.gamma.domain.authority.data.AuthorityRepository;
import it.chalmers.gamma.domain.authority.exception.AuthorityAlreadyExists;
import it.chalmers.gamma.domain.authority.exception.AuthorityNotFoundException;
import it.chalmers.gamma.domain.authoritylevel.AuthorityLevelName;
import it.chalmers.gamma.domain.authoritylevel.service.AuthorityLevelService;
import it.chalmers.gamma.domain.supergroup.data.SuperGroupDTO;

import it.chalmers.gamma.domain.post.data.PostDTO;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final AuthorityLevelService authorityLevelService;
    private final AuthorityFinder authorityFinder;

    public AuthorityService(AuthorityRepository authorityRepository,
                            AuthorityLevelService authorityLevelService,
                            AuthorityFinder authorityFinder) {
        this.authorityRepository = authorityRepository;
        this.authorityLevelService = authorityLevelService;
        this.authorityFinder = authorityFinder;
    }

    public void createAuthority(UUID superGroupId, UUID postId, AuthorityLevelName authorityLevelName)
                throws AuthorityAlreadyExists {
        if(this.authorityFinder.authorityExists(superGroupId, postId, authorityLevelName)) {
            throw new AuthorityAlreadyExists();
        }

        this.authorityRepository.save(
                new Authority(
                        new AuthorityPK(superGroupId, postId, authorityLevelName.value)
                )
        );
    }

}
