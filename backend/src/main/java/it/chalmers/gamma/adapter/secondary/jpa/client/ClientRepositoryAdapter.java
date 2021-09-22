package it.chalmers.gamma.adapter.secondary.jpa.client;

import it.chalmers.gamma.app.client.ClientRepository;
import it.chalmers.gamma.domain.client.Client;
import it.chalmers.gamma.domain.client.ClientId;
import it.chalmers.gamma.domain.client.ClientSecret;
import it.chalmers.gamma.domain.user.UserId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientRepositoryAdapter implements ClientRepository {

    private final ClientJpaRepository repository;

    public ClientRepositoryAdapter(ClientJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(Client client, ClientSecret clientSecret) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(ClientId clientId) throws ClientNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClientSecret resetClientSecret(ClientId clientId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Client> getAll() {
        return this.repository.findAll().stream().map(ClientEntity::toDomain).toList();
    }

    @Override
    public Optional<Client> get(ClientId clientId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Client> getClientsByUserApproved(UserId id) {
        throw new UnsupportedOperationException();
    }
}
