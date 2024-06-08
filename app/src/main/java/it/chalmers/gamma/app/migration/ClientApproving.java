package it.chalmers.gamma.app.migration;

import it.chalmers.gamma.app.authentication.AccessGuard;
import it.chalmers.gamma.app.client.domain.ClientRepository;
import it.chalmers.gamma.app.client.domain.ClientUid;
import it.chalmers.gamma.app.user.domain.UserId;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.chalmers.gamma.app.authentication.AccessGuard.isAdmin;

@Service
public class ClientApproving {

    private final AccessGuard accessGuard;
    private final ClientRepository clientRepository;

    public ClientApproving(AccessGuard accessGuard, ClientRepository clientRepository) {
        this.accessGuard = accessGuard;
        this.clientRepository = clientRepository;
    }

    public void approve(ClientUid clientUid, List<UserId> userIds) {
        accessGuard.require(isAdmin());

        for (UserId userId : userIds) {
            try {
                clientRepository.addClientApproval(userId, clientUid);
            } catch(Exception e) {
                //ignore...
            }
        }
    }

}
