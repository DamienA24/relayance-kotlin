Feature: Add a new customer to the customer list

  As a sales representative
  I want to be able to add a new customer
  So that I can track new opportunities

  Scenario: Add a customer with valid information
    Given the user is on the add customer screen
    When the user enters the name "John Smith"
    And the user enters the email "john.smith@example.com"
    And the user clicks the add button
    Then a success confirmation is displayed
    And the customer "John Smith" appears in the customer list

  Scenario: Validate email format
    Given the user is on the add customer screen
    When the user enters the name "Jane Doe"
    And the user enters the email "invalid-email"
    And the user clicks the add button
    Then an error message is displayed
    And the customer is not added to the list
