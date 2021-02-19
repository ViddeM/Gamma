package it.chalmers.gamma.domain.whitelist.service;

import it.chalmers.gamma.domain.Cid;
import it.chalmers.gamma.domain.user.service.UserFinder;
import it.chalmers.gamma.domain.whitelist.data.Whitelist;
import it.chalmers.gamma.domain.whitelist.data.WhitelistRepository;
import it.chalmers.gamma.domain.whitelist.exception.CidNotWhitelistedException;
import it.chalmers.gamma.domain.whitelist.exception.CidAlreadyWhitelistedException;

import it.chalmers.gamma.domain.whitelist.exception.UserAlreadyExistsWithCidException;
import org.springframework.stereotype.Service;

@Service
public class WhitelistService {

    private final UserFinder userFinder;
    private final WhitelistFinder whitelistFinder;
    private final WhitelistRepository whitelistRepository;

    public WhitelistService(UserFinder userFinder,
                            WhitelistFinder whitelistFinder,
                            WhitelistRepository whitelistRepository) {
        this.userFinder = userFinder;
        this.whitelistFinder = whitelistFinder;
        this.whitelistRepository = whitelistRepository;
    }

    public void addWhiteListedCid(Cid cid) throws CidAlreadyWhitelistedException, UserAlreadyExistsWithCidException {
        if(this.userFinder.userExists(cid)) {
            throw new UserAlreadyExistsWithCidException();
        }

        if (this.whitelistFinder.cidIsWhitelisted(cid)) {
            throw new CidAlreadyWhitelistedException();
        }

        Whitelist whitelist = new Whitelist(cid);
        this.whitelistRepository.save(whitelist);
    }

    public void removeWhiteListedCid(Cid cid) throws CidNotWhitelistedException {
        if (!this.whitelistFinder.cidIsWhitelisted(cid)) {
            throw new CidNotWhitelistedException();
        }

        this.whitelistRepository.deleteById(cid);
    }

}
