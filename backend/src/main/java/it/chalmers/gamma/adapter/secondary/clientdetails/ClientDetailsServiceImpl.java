package it.chalmers.gamma.adapter.secondary.clientdetails;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
//        return this.clientRepository.findById(ClientId.valueOf(clientId))
//                .map(clientEntity -> new ClientDetailsImpl(clientEntity.toDomain(), clientEntity.getClientSecret()))
//                .orElseThrow(ClientAdminController.ClientNotFoundResponse::new);
        return null;
    }

}
