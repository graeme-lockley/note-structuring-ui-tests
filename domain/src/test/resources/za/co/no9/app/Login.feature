Feature: Client Login

  In order to protect our client's accounts and to manage our financial and brand risk it is necessary that
  clients login to the application using a credential set of user name and password.

  Scenario: Successful login
    Given a registered client with the user name andrew and password password
    When I login with the credential andrew/password
    Then the login result is successful

  Scenario: Unknown client login
    When I login with the credential andrew/password
    Then the login result is UNKNOWN_CLIENT_ID

  Scenario: The correct user name is used but an incorrect password is applied
    Given a registered client with the user name andrew and password password
    When I login with the credential andrew/wrongpassword
    Then the login result is INVALID_CREDENTIAL
