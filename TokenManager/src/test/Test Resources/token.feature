Feature: Tokens
  Scenario: generate a list of 3 tokens
    Given request 3 tokens
    Then the tokens list is generated
    And 3 unique tokens is returned

  Scenario: generate a list of 6 tokens
    Given request 6 tokens
    Then the tokens list is not generated
    And error message is "Must create between 1 and 5 tokens"

  Scenario: generate a list of 0 tokens
    Given request 0 tokens
    Then the tokens list is not generated
    And error message is "Must create between 1 and 5 tokens"

#  Scenario: tokens requested from customer not registered in DTUPay
#  Given a list of 5 tokens is requested by a customer
#  Then the TokenManager check whether the customer exist
#  And error message is "customer not exist"

  Scenario: customer with more than 1 token request tokens
    Given a list of 3 tokens is requested by a customer
    When the customer request to generate 4 new tokens
    Then error message is "User already has more than 1 token"

  Scenario: customer with more than 1 token request tokens
    Given a list of 1 tokens is requested by a customer
    Then the customer have 1 tokens in the list
    When the customer request to generate 4 new tokens
    Then the customer have 5 tokens in the list
    When the customer request to generate 1 new tokens
    Then error message is "User already has more than 1 token"
