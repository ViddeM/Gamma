package it.chalmers.gamma.adapter.secondary.authenticated;

import it.chalmers.gamma.adapter.secondary.userdetails.UserDetailsProxy;
import it.chalmers.gamma.app.repository.ApiKeyRepository;
import it.chalmers.gamma.app.authentication.ApiAuthenticated;
import it.chalmers.gamma.app.authentication.Authenticated;
import it.chalmers.gamma.app.authentication.AuthenticatedService;
import it.chalmers.gamma.app.authentication.Unauthenticated;
import it.chalmers.gamma.app.authentication.InternalUserAuthenticated;
import it.chalmers.gamma.app.repository.ClientRepository;
import it.chalmers.gamma.app.repository.UserRepository;
import it.chalmers.gamma.app.domain.apikey.ApiKey;
import it.chalmers.gamma.app.domain.apikey.ApiKeyToken;
import it.chalmers.gamma.app.domain.client.Client;
import it.chalmers.gamma.app.domain.user.Cid;
import it.chalmers.gamma.app.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticatedServiceAdapter implements AuthenticatedService {

    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ClientRepository clientRepository;

    public AuthenticatedServiceAdapter(UserRepository userRepository,
                                       ApiKeyRepository apiKeyRepository,
                                       ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Authenticated getAuthenticated() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return new Unauthenticated() {};
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsProxy userDetailsProxy) {
            User user = this.userRepository.get(Cid.valueOf(userDetailsProxy.getUsername())).orElseThrow(IllegalStateException::new);
            return (InternalUserAuthenticated) () -> user;
        }

        if (principal instanceof ApiKeyToken apiKeyToken) {
            ApiKey apiKey = this.apiKeyRepository.getByToken(apiKeyToken).orElseThrow(IllegalStateException::new);
            Optional<Client> maybeClient = this.clientRepository.getByApiKey(apiKeyToken);
            return new ApiAuthenticated() {
                @Override
                public ApiKey get() {
                    return apiKey;
                }

                @Override
                public Optional<Client> getClient() {
                    return maybeClient;
                }
            };
        }

        return new Unauthenticated() {};
    }
}
