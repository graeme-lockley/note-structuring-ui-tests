package za.co.no9.app.aggregate.client;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.aggregate.client.ClientService.ClientServiceFailure;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Password;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.Optional;

import static org.junit.Assert.*;

public class ClientServiceTest {
    private static final ClientID VALID_CLIENT_NAME = new ClientID("graeme");
    private static final Password VALID_CLIENT_PASSWORD = new Password("password");
    private static final Password INVALID_CLIENT_PASSWORD = new Password("wrong-password");

    private static final Credential UNKNOWN_CLIENT_CREDENTIAL = Credential.from(new ClientID("jimmy"), new Password("bob's your uncle"));
    private static final Credential VALID_CLIENT_CREDENTIAL = Credential.from(VALID_CLIENT_NAME, VALID_CLIENT_PASSWORD);
    private static final Credential INVALID_CLIENT_CREDENTIAL = Credential.from(new ClientID("graeme"), INVALID_CLIENT_PASSWORD);

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

    @Test
    public void given_a_known_client_should_login() throws Exception {
        Optional<ClientServiceFailure> loginResult = clientService.login(VALID_CLIENT_CREDENTIAL);

        assertFalse(loginResult.isPresent());
    }

    @Test
    public void given_an_invalid_client_should_not_login() throws Exception {
        Optional<ClientServiceFailure> loginResult = clientService.login(UNKNOWN_CLIENT_CREDENTIAL);

        assertTrue(loginResult.isPresent());
        assertEquals(ClientServiceFailure.UNKNOWN_CLIENT, loginResult.get());
    }

    @Test
    public void should_not_login_if_the_clients_password_is_invalid() throws Exception {
        Optional<ClientServiceFailure> loginResult = clientService.login(INVALID_CLIENT_CREDENTIAL);

        assertTrue(loginResult.isPresent());
        assertEquals(ClientServiceFailure.INVALID_CREDENTIAL, loginResult.get());
    }
}