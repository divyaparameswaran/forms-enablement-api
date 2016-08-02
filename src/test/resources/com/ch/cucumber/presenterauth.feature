Feature: presenter authentication from chips

  Scenario: As an API consumer I want to authenticate the presenter prior to sending the form to CHIPS
    Given I submit a valid credentials to the presenter auth endpoint
    Then I should receive a successful response from the presenter auth endpoint

    Given I submit invalid credentials to the presenter auth endpoint
    Then I should receive an unsuccessful response from the presenter auth endpoint

    Given I submit a package with valid presenter credentials
    Then The forms with account payment should be given an account number

    Given I submit a package with valid presenter credentials
    Then The forms with account payment should be given an account number

    Given I submit a package with invalid presenter credentials
    Then An exception should be thrown and no submision should take place