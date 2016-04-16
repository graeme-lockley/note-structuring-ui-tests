Feature: Inter-account Funds Transfer

  Provide the ability to allow clients to move money between their different accounts. This feature does not consider
  the more general scenario of transferring money between accounts that do not belong to the client.

  Background:
    Given a registered client with the user name andrew and password password
    And the client andrew has the current account 10001235 with opening balance R123.45
    And the client andrew has the current account 10001236 with opening balance R0.00
    And the client andrew has the current account 10001237 with opening balance USD0.00

  Scenario: Unknown source and destination accounts
    When andrew transfers R12.00 from 10001000 to 10001001
    Then the transfer fails with the error UNKNOWN_SOURCE_ACCOUNT
    And the transfer fails with the error UNKNOWN_DESTINATION_ACCOUNT

  Scenario: Unknown client
    When rodger transfers R12.00 from 10001000 to 1001001
    Then the transfer fails with the error UNKNOWN_CLIENT

  Scenario: Insufficient funds
    When andrew transfers R12345.00 from 10001235 to 10001236
    Then the transfer fails with the error INSUFFICIENT_FUNDS

  Scenario: Currency mismatch
    When andrew transfers R100.00 from 10001235 to 10001237
    Then the transfer fails with the error CURRENCY_MISMATCH

  Scenario: Successful transfer
    When andrew transfers R100.00 from 10001235 to 10001236 with description "Test Payment"
    Then the transfer succeeds
    And the account 10001235 has a balance of R23.45
    And the account 10001236 has a balance of R100.00
    And the account 10001235 has a debit transaction of R100.00 with description "Test Payment"
    And the account 10001236 has a credit transaction of R100.00 with description "Test Payment"
    And andrew has an inter account transfer audit trail item:
      | name                | value        |
      | amount              | ZAR100.00    |
      | description         | Test Payment |
      | source account      | 10001235     |
      | destination account | 10001236     |
