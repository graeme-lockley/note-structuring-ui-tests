package za.co.no9.app;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import za.co.no9.app.aggregate.client.AddAccountCommand;
import za.co.no9.app.aggregate.client.AddClientCommand;
import za.co.no9.app.aggregate.client.ClientService;
import za.co.no9.app.aggregate.transfer.InterAccountTransferCommand;
import za.co.no9.app.aggregate.transfer.TransferService;
import za.co.no9.app.domain.*;
import za.co.no9.app.read.AuditEntry;
import za.co.no9.app.read.Credential;
import za.co.no9.app.read.ReadService;
import za.co.no9.app.read.Transaction;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;
import za.co.no9.app.util.EventStore;
import za.co.no9.app.util.Tuple;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class StepDefinitions {
    private static Map<String, Predicate<Tuple<AuditEntry, NameValue>>> AUDIT_TRAIL_FIELD_TESTS = new HashMap<>();

    static {
        AUDIT_TRAIL_FIELD_TESTS.put("amount", a -> a._1.amount.asString().equals(a._2.value));
        AUDIT_TRAIL_FIELD_TESTS.put("description", a -> a._1.description.asString().equals(a._2.value));
        AUDIT_TRAIL_FIELD_TESTS.put("source account", a -> a._1.sourceAccount.asString().equals(a._2.value));
        AUDIT_TRAIL_FIELD_TESTS.put("destination account", a -> a._1.destinationAccount.asString().equals(a._2.value));
    }

    private Optional<Boolean> loginResult = Optional.empty();
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

    @Given("^a registered client with the user name ([^ ]+) and password (.*)$")
    public void a_registered_client_with_the_user_name_andrew_and_password_password(String username, String password) throws Throwable {
        final Optional<ClientService.ClientServiceFailure> failure = DI.get(API.class).addClient(new AddClientCommand(new ClientID(username), new Password(password)));
        assertFalse(failure.isPresent());
    }

    @Given("^a registered client with the user name ([^ ]+)$")
    public void a_registered_client_with_the_user_name_andrew(String username) throws Throwable {
        final Optional<ClientService.ClientServiceFailure> failure = DI.get(API.class).addClient(new AddClientCommand(new ClientID(username), new Password("password")));
        assertFalse(failure.isPresent());
    }

    @When("^I login with the credential (.*)/(.*)")
    public void I_login_with_the_credential_andrew_password(String username, String password) throws Throwable {
        loginResult = Optional.of(DI.get(API.class).login(Credential.from(new ClientID(username), new Password(password))));
    }

    @Then("^the login is successful$")
    public void The_login_is_successful() throws Throwable {
        assertTrue(loginResult.isPresent());
        assertTrue(loginResult.get());
    }

    @Then("^the login is unsuccessful$")
    public void The_login_is_unsuccessful() throws Throwable {
        assertTrue(loginResult.isPresent());
        assertFalse(loginResult.get());
    }

    @Given("^(.+) has a current account (.+) with opening balance (.+)$")
    public void client_has_a_current_account_with_opening_balance_(String clientID, String accountRef, String openBalance) throws Throwable {
        DI.get(API.class).addAccount(new AddAccountCommand(
                new ClientID(clientID),
                new AccountRef(accountRef),
                Money.from(openBalance),
                new AccountName("Account Name")));
    }

    @When("^(.+) transfers (.+) from (.+) to ([^ ]+) with description \"(.+)\"$")
    public void andrew_transfers_R_from_to(String clientID, String transferAmount, String sourceAccountRef, String destinationAccountRef, String description) throws Throwable {
        transferResult = DI.get(API.class).interAccountTransfer(new InterAccountTransferCommand(
                new ClientID(clientID),
                new AccountRef(sourceAccountRef),
                new AccountRef(destinationAccountRef),
                Money.from(transferAmount),
                new TransactionDescription(description)
        ));
    }

    @When("^(.+) transfers (.+) from (.+) to ([^ ]+)$")
    public void andrew_transfers_R_from_to(String clientID, String transferAmount, String sourceAccountRef, String destinationAccountRef) throws Throwable {
        transferResult = DI.get(API.class).interAccountTransfer(new InterAccountTransferCommand(
                new ClientID(clientID),
                new AccountRef(sourceAccountRef),
                new AccountRef(destinationAccountRef),
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
        final Either<ReadService.ReadServiceFailure, Money> accountBalanceEither = DI.get(API.class).accountBalance(new AccountRef(accountRef));
        assertTrue(accountBalanceEither.toString(), accountBalanceEither.isRight());
        assertEquals(Money.from(accountBalance), accountBalanceEither.right());
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
        final Either<ReadService.ReadServiceFailure, Stream<Transaction>> accountTransactionsEither = DI.get(API.class).accountTransactions(accountRef);
        assertTrue(accountTransactionsEither.toString(), accountTransactionsEither.isRight());
        assertTrue(accountTransactionsEither.right().anyMatch(t -> t.isDebit == isDebit && t.amount.equals(amount) && t.description.equals(description)));
    }

    @And("^([^ ]+) has an inter-account transfer audit entry:$")
    public void andrew_has_an_inter_account_transfer_audit_trail_item(String clientID, DataTable dataTable) throws Throwable {
        List<NameValue> items = dataTable.asList(NameValue.class);
        final Either<ReadService.ReadServiceFailure, Stream<AuditEntry>> auditTrailEither = DI.get(API.class).auditTrail(new ClientID(clientID));
        assertTrue(auditTrailEither.toString(), auditTrailEither.isRight());
        assertTrue(auditTrailEither.right().anyMatch(auditEntry -> matchAuditEntry(auditEntry, items)));
    }

    private boolean matchAuditEntry(AuditEntry auditEntry, Collection<NameValue> items) {
        boolean result = true;
        for (NameValue nameValue : items) {
            final Predicate<Tuple<AuditEntry, NameValue>> tuplePredicate = AUDIT_TRAIL_FIELD_TESTS.get(nameValue.name);

            if (tuplePredicate == null) {
                fail("Unknown audit trail field: " + nameValue.name + ": Valid values: " + AUDIT_TRAIL_FIELD_TESTS.keySet().stream().collect(Collectors.joining(", ")));
                result = false;
            } else {
                result = result && tuplePredicate.test(Tuple.from(auditEntry, nameValue));
            }
        }
        return result;
    }
}
