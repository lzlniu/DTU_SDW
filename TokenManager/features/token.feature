Feature: Tokens

Scenario: generate a list of tokens (3)
  Given a customer request 3 tokens
  When the tokens list is generated
  Then 3 unique tokens is returned

Scenario: generate a list of tokens (6)
  Given a customer request 6 tokens
  When the tokens list is generated
  Then error with message "Must create between 1 and 5 tokens"

Scenario: generate a list of tokens (0)
  Given a customer request 0 tokens
  When the tokens list is generated
  Then error message is "Must create between 1 and 5 tokens"

Scenario: tokens requested from customer not registered in DTUPay
  Given a list of 5 tokens is requested
  When the TokenManager check whether the customer exist
  Then error message is "customer not exist"

Scenario: customer with more than 1 token request tokens
  Given a customer with 3 tokens
  When the customer request to generate new tokens list
  Then error message is "User already has more than 1 token"