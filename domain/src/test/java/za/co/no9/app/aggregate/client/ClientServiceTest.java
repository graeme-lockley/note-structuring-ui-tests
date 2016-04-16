package za.co.no9.app.aggregate.client;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.aggregate.client.ClientService.ClientServiceFailure;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Password;
import za.co.no9.app.read.Credential;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import static org.junit.Assert.assertEquals;

public class ClientServiceTest {
    private static final ClientID VALID_CLIENT_NAME = new ClientID("graeme");
    private static final Password VALID_CLIENT_PASSWORD = new Password("password");
    private static final Credential VALID_CLIENT_CREDENTIAL = Credential.from(VALID_CLIENT_NAME, VALID_CLIENT_PASSWORD);

    private ClientService clientService = new ClientService();
    private EventStore eventStore = new EventStore();

    @Before
    public void setup() {
        DI.reset();
        DI.register(eventStore);
        DI.register(clientService);

        eventStore.registerEventHandler(clientService);

        clientService.addClient(new AddClientCommand(VALID_CLIENT_CREDENTIAL.clientID(), VALID_CLIENT_PASSWORD));
    }

    @Test
    public void should_throw_an_exception_when_adding_a_duplicate_client() throws Exception {
        assertEquals(ClientServiceFailure.DUPLICATE_CLIENT_ID, clientService.addClient(new AddClientCommand(VALID_CLIENT_CREDENTIAL.clientID(), VALID_CLIENT_PASSWORD)).get());
    }
}