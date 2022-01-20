Feature: Report logging
  #@author s212643 - Xingguang Geng
  Scenario: Logging a successful payment
    Given a payment with value of 42.0
    When the "SuccessfulPayment" event is sent with that payment
    Then the log contains that payment with value of 42.0
  #@author s164422 - Thomas Bergen
  Scenario: Logging a successful refund
    Given a refund with value of 312.0
    When the "SuccessfulRefund" event is sent with that refund
    Then the log contains that refund with value of -312.0