Feature: form submission from salesforce

  Scenario: As an API consumer I want to post my form to the Forms Service API so it can be processed on CHIPS
    Given I submit a invalid form to the forms API using the correct credentials
    Then then I should receive an error message from the API
