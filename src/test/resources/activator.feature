Feature: activator request

  Scenario: As a member of companies house staff I want to request forms from the queue
    Given The queue contains 3 packages all pending
    When  I request 2 pending packages
    Then  Two packages status should be changed
