package za.co.no9.app;

import za.co.no9.app.aggregate.client.AddClientCommand;
import za.co.no9.app.aggregate.client.ClientService;
import za.co.no9.app.aggregate.client.Credential;
import za.co.no9.app.util.DI;

import java.util.Optional;

public class API {
    public Optional<ClientService.ClientServiceFailure> addClient(AddClientCommand command) {
        return DI.get(ClientService.class).addClient(command);
    }

    public Optional<ClientService.ClientServiceFailure> login(Credential credential) {
        return DI.get(ClientService.class).login(credential);
    }
}
