Feature: Report logging
  Scenario: Logging a successful payment
    Given a payment
    When the "SuccessfulPayment" event is sent with that payment
    Then the log contains that payment