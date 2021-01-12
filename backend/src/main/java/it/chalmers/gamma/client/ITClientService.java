package it.chalmers.gamma.client;

import it.chalmers.gamma.domain.text.Text;
import it.chalmers.gamma.client.response.ITClientDoesNotExistException;
import it.chalmers.gamma.util.TokenUtils;

import it.chalmers.gamma.util.UUIDUtil;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ITClientService implements ClientDetailsService {

    @Value("${application.auth.accessTokenValidityTime}")       // TODO Fix this
    private int accessTokenValidityTime;

    @Value("${application.auth.refreshTokenValidityTime}")
    private int refreshTokenValidityTime;

    private final ITClientRepository itClientRepository;

    public ITClientService(ITClientRepository itClientRepository) {
        this.itClientRepository = itClientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        return this.itClientRepository.findByClientId(clientId).orElseThrow(ITClientDoesNotExistException::new)
            .toDTO();
    }

    public ITClientDTO createITClient(String name, Text description, String redirect, boolean autoApprove) {
        ITClient client = new ITClient();
        client.setName(name);
        client.setDescription(description == null ? new Text() : description);
        client.setWebServerRedirectUri(redirect);
        client.setCreatedAt(Instant.now());
        client.setLastModifiedAt(Instant.now());
        client.setAccessTokenValidity(this.accessTokenValidityTime);
        client.setAutoApprove(autoApprove);
        client.setRefreshTokenValidity(this.refreshTokenValidityTime);
        client.setClientId(TokenUtils.generateToken(75, TokenUtils.CharacterTypes.LOWERCASE,
                TokenUtils.CharacterTypes.UPPERCASE,
                TokenUtils.CharacterTypes.NUMBERS)
        );
        String clientSecret = TokenUtils.generateToken(75, TokenUtils.CharacterTypes.LOWERCASE,
                TokenUtils.CharacterTypes.UPPERCASE,
                TokenUtils.CharacterTypes.NUMBERS);
        client.setClientSecret("{noop}" + clientSecret);
        return this.itClientRepository.save(client).toDTO();
    }

    public List<ITClientDTO> getAllClients() {
        return this.itClientRepository.findAll().stream().map(ITClient::toDTO).collect(Collectors.toList());
    }


    public void removeITClient(UUID id) {
        this.itClientRepository.deleteById(id);
    }

    public void editClient(UUID id, ITClientDTO clientDTO) {
        ITClient client = this.itClientRepository.findById(id).orElseThrow();
        client.setLastModifiedAt(Instant.now());
        client.setName(clientDTO.getName() == null ? client.getName() : clientDTO.getName());
        client.setDescription(clientDTO.getDescription() == null
                ? client.getDescription() : clientDTO.getDescription());
        client.setWebServerRedirectUri(clientDTO.getWebServerRedirectUri() == null
                ? client.getWebServerRedirectUri() : clientDTO.getWebServerRedirectUri());
    }

    public boolean clientExists(String id) {
        return UUIDUtil.validUUID(id) && this.itClientRepository.existsById(UUID.fromString(id))
            || this.itClientRepository.existsITClientByClientId(id);
    }


    public void addITClient(ITClient itClient) {
        this.itClientRepository.save(itClient);
    }

}
