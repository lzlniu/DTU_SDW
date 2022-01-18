Feature: Tokens
  #@author s213578 - Johannes Pedersen
  Scenario: generate a list of 3 tokens
    Given request 3 tokens
    Then the tokens list is generated
    And 3 unique tokens is returned
  #@author s212643 - Xingguang Geng
  Scenario: generate a list of 6 tokens
    Given request 6 tokens
    Then the tokens list is not generated
    And error message is "Must create between 1 and 5 tokens"
  #@author s164422 - Thomas Bergen
  Scenario: generate a list of 0 tokens
    Given request 0 tokens
    Then the tokens list is not generated
    And error message is "Must create between 1 and 5 tokens"
  #@author s174293 - Kasper Jørgensen
  Scenario: customer with more than 1 token request tokens
    Given a list of 3 tokens is requested by a customer
    And the customer is registered
    When the customer request to generate 4 new tokens
    #Then the "GetTokensRequested" event is sent
    When the "CustomerRegisteredForTokens" event is recieved
    Then response is received from manager
    And error message is "User already has more than 1 token"
  #@author s202772 - Gustav Kinch
  Scenario: customer with more than 1 token request tokens 2
    Given a list of 1 tokens is requested by a customer
    And the customer is registered
    Then the customer have 1 tokens in the list
    When the customer request to generate 4 new tokens
    And the "CustomerRegisteredForTokens" event is recieved
    Then response is received from manager
    And the customer have 5 tokens in the list
    When the customer request to generate 1 new tokens
    And the "CustomerRegisteredForTokens" event is recieved
    Then response is received from manager
    And error message is "User already has more than 1 token"
  #@author s215949 - Zelin Li
  Scenario: tokens requested from customer not registered in DTUPay
    Given a list of 1 tokens is requested by a customer
    And the customer is not registered
    When the customer request to generate 4 new tokens
    And the "CustomerRegisteredForTokens" event is recieved
    Then response is received from manager
    And error message is "customer is not registered"
