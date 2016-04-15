package za.co.no9.app;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import za.co.no9.app.aggregate.client.AddAccountCommand;
import za.co.no9.app.aggregate.client.AddClientCommand;
import za.co.no9.app.aggregate.client.ClientService;
import za.co.no9.app.aggregate.client.Credential;
import za.co.no9.app.aggregate.transfer.InterAccountTransferCommand;
import za.co.no9.app.aggregate.transfer.TransferService;
import za.co.no9.app.domain.*;
import za.co.no9.app.read.ReadService;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class StepDefinitions {
    private Optional<ClientService.ClientServiceFailure> loginResult;
    private Optional<Set<TransferService.PaymentServiceFailure>> transferResult;

    public StepDefinitions() {
        DI.reset();
        DI.register(new API());
        DI.register(new EventStore());
        DI.register(new ClientService());
        DI.register(new TransferService());
        DI.register(new ReadService());

        DI.get(EventStore.class).registerEventHandler(DI.get(ClientService.class));
        DI.get(EventStore.class).registerEventHandler(DI.get(TransferService.class));
        DI.get(EventStore.class).registerEventHandler(DI.get(ReadService.class));
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

    @Given("^the client (.+) has the current account (.+) with opening balance (.+)$")
    public void the_client_andrew_has_the_current_account_with_opening_balance_(String clientID, String accountRef, String openBalance) throws Throwable {
        DI.get(API.class).addAccount(new AddAccountCommand(
                new ClientID(clientID),
                new AccountRef(accountRef),
                Money.from(openBalance),
                new AccountName("Account Name")));
    }

    @When("^(.+) transfers (.+) from (.+) to ([^ ]+) with description \"(.+)\"$")
    public void andrew_transfers_R_from_to(String clientID, String transferAmount, String sourceAccountRef, String targetAccountRef, String description) throws Throwable {
        transferResult = DI.get(API.class).interAccountTransfer(new InterAccountTransferCommand(
                new ClientID(clientID),
                new AccountRef(sourceAccountRef),
                new AccountRef(targetAccountRef),
                Money.from(transferAmount),
                new TransactionDescription(description)
        ));
    }

    @When("^(.+) transfers (.+) from (.+) to ([^ ]+)$")
    public void andrew_transfers_R_from_to(String clientID, String transferAmount, String sourceAccountRef, String targetAccountRef) throws Throwable {
        transferResult = DI.get(API.class).interAccountTransfer(new InterAccountTransferCommand(
                new ClientID(clientID),
                new AccountRef(sourceAccountRef),
                new AccountRef(targetAccountRef),
                Money.from(transferAmount),
                new TransactionDescription("Default")
        ));
    }

    @Then("^the transfer succeeds$")
    public void the_transfer_succeeds() throws Throwable {
        assertFalse("Transfer was unsuccessful: " + transferResult.toString(), transferResult.isPresent());
    }

    @And("^the account (\\d+) has a balance of (.+)$")
    public void the_account_has_a_balance_of_R_(String accountRef, String accountBalance) throws Throwable {
        assertEquals(Money.from(accountBalance), DI.get(API.class).accountBalance(new AccountRef(accountRef)));
    }

    @Then("^the transfer fails with the error (.+)$")
    public void the_transfer_fails_with_the_error_(String transferError) throws Throwable {
        assertTrue(this.transferResult.isPresent());
        assertTrue("Transfer Result Errors: " + this.transferResult.toString(), this.transferResult.get().contains(TransferService.PaymentServiceFailure.valueOf(transferError)));
    }

    @Then("^the account ([^ ]+) has a debit transaction of ([^ ]+) with description \"([^\"]*)\"$")
    public void the_account_has_a_debit_transaction_of_R_with_reference(String accountRef, String amount, String description) throws Throwable {
        assertTransaction(new AccountRef(accountRef), true, Money.from(amount), new TransactionDescription(description));
    }

    @Then("^the account ([^ ]+) has a credit transaction of ([^ ]+) with description \"([^\"]*)\"$")
    public void the_account_has_a_credit_transaction_of_R_with_reference(String accountRef, String amount, String description) throws Throwable {
        assertTransaction(new AccountRef(accountRef), false, Money.from(amount), new TransactionDescription(description));
    }

    private void assertTransaction(AccountRef accountRef, boolean isDebit, Money amount, TransactionDescription description) throws Throwable {
        assertTrue(DI.get(API.class).accountTransactions(accountRef).anyMatch(t -> t.isDebit == isDebit && t.amount.equals(amount) && t.description.equals(description)));
    }

    @And("^([^ ]+) has an inter account transfer audit trail item:$")
    public void andrew_has_an_inter_account_transfer_audit_trail_item(String clientID, DataTable dataTable) throws Throwable {
        List<NameValue> items = dataTable.asList(NameValue.class);
    }
}
