package za.co.no9.app;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import za.co.no9.app.aggregate.client.AddClientCommand;
import za.co.no9.app.aggregate.client.ClientService;
import za.co.no9.app.aggregate.client.Credential;
import za.co.no9.app.aggregate.transfer.TransferService;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Password;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.Optional;

import static org.junit.Assert.*;

public class StepDefinitions {
    private Optional<ClientService.ClientServiceFailure> loginResult;

    public StepDefinitions() {
        DI.reset();
        DI.register(new API());
        DI.register(new EventStore());
        DI.register(new ClientService());
        DI.register(new TransferService());

        DI.get(EventStore.class).registerEventHandler(DI.get(ClientService.class));
        DI.get(EventStore.class).registerEventHandler(DI.get(TransferService.class));
    }

    @Given("^a registered client with the user name (.*) and password (.*)$")
    public void a_registered_client_with_the_user_name_andrew_and_password_password(String username, String password) throws Throwable {
        final Optional<ClientService.ClientServiceFailure> failure = DI.get(API.class).addClient(new AddClientCommand(new ClientID(username), new Password(password)));

        assertFalse(failure.isPresent());
    }

    @When("^I login with the credential (.*)/(.*)")
    public void I_login_with_the_credential_andrew_password(String username, String password) throws Throwable {
        loginResult = DI.get(API.class).login(Credential.from(new ClientID(username), new Password(password)));
    }

    @Then("^the login result is (.*)$")
    public void The_login_is_successful(String loginResult) throws Throwable {
        if (loginResult.equalsIgnoreCase("successful")) {
            assertFalse(this.loginResult.isPresent());
        } else {
            assertTrue(this.loginResult.isPresent());
            assertEquals(this.loginResult.get().name(), loginResult);
        }
    }
}
