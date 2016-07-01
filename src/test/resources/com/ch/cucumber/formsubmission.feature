Feature: form submission from salesforce

  Scenario: Invalid submission
    Given I submit a invalid form to the forms API using the correct credentials
    Then I should receive an error response from the API

  Scenario: Valid submission
    Given I submit a valid form to the forms API using the correct credentials
    Then the package and forms should be added to the database
    Then I should get a success response from the API
