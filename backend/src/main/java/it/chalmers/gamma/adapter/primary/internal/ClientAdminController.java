package it.chalmers.gamma.adapter.primary.internal;

import it.chalmers.gamma.app.repository.ClientRepository;
import it.chalmers.gamma.app.facade.internal.ClientFacade;

import it.chalmers.gamma.util.response.NotFoundResponse;
import it.chalmers.gamma.util.response.SuccessResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/admin/clients")
public final class ClientAdminController {

    private final ClientFacade clientFacade;

    public ClientAdminController(ClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    private record CreateClientRequest(String webServerRedirectUrl,
                                       String prettyName,
                                       String svDescription,
                                       String enDescription,
                                       boolean generateApiKey,
                                       List<String> restrictions,
                                       boolean emailScope
    ) { }

    @PostMapping()
    public ClientFacade.ClientAndApiKeySecrets addClient(@RequestBody CreateClientRequest request) {
        return this.clientFacade.create(
                new ClientFacade.NewClient(
                        request.webServerRedirectUrl,
                        request.prettyName,
                        request.svDescription,
                        request.enDescription,
                        request.generateApiKey,
                        request.restrictions,
                        request.emailScope
                )
        );
    }

    //TODO: Reflect that clientUid is used and not clientId in the frontend
    @PostMapping("/{clientId}/reset")
    public String resetClientCredentials(@PathVariable("clientId") String clientId) {
        String clientSecret;

        try {
            clientSecret = this.clientFacade.resetClientSecret(clientId);
        } catch (ClientFacade.ClientNotFoundException e) {
            throw new ClientNotFoundResponse();
        }

        return clientSecret;
    }

    @GetMapping()
    public List<ClientFacade.ClientDTO> getClients() {
        return this.clientFacade.getAll();
    }

    @GetMapping("/{clientId}")
    public ClientFacade.ClientDTO getClient(@PathVariable("clientId") String id) {
        return this.clientFacade.get(id).orElseThrow(ClientNotFoundResponse::new);
    }

    //TODO: Reflect that clientUid is used and not clientId in the frontend
    @DeleteMapping("/{clientUid}")
    public ClientDeletedResponse deleteClient(@PathVariable("clientUid") String id) {
        try {
            this.clientFacade.delete(id);
            return new ClientDeletedResponse();
        } catch (ClientRepository.ClientNotFoundException e) {
            throw new ClientNotFoundResponse();
        }
    }

    private static class ClientDeletedResponse extends SuccessResponse { }

    public static class ClientNotFoundResponse extends NotFoundResponse { }
    
}
