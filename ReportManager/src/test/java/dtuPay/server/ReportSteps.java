package dtuPay.server;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import objects.DtuPayUser;
import objects.Payment;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ReportSteps {
    MessageQueue queue = mock(MessageQueue.class);
    ReportManager manager = new ReportManager(queue);
    Payment p, r;
    DtuPayUser customer;

    //@author s202772 - Gustav Kinch
    @Given("a payment with value of {bigdecimal}")
    public void aPayment(BigDecimal amount) {
        p = new Payment("customer", "merchant", amount);
        customer = new DtuPayUser("I", "Payed", "myID", "bankID", "123456-7890");
    }
    //@author s215949 - Zelin Li
    @When("the {string} event is sent with that payment")
    public void theEventIsSentWithThatPayment(String arg0) {
        manager.logPayment(new Event(arg0, new Object[]{p, customer.getDtuPayID()}));
    }
    //@author s213578 - Johannes Pedersen
    @Then("the log contains that payment with value of {bigdecimal}")
    public void theLogContainsThatPayment(BigDecimal amount) {
        assertTrue(manager.getPayments().contains(p));
        assertEquals(amount, p.getAmount());
    }
    //@author s212643 - Xingguang Geng
    @Given("a refund with value of {bigdecimal}")
    public void aRefund(BigDecimal amount) {
        r = new Payment("customerB", "merchantB", amount);
        customer = new DtuPayUser("Tom", "Joe", "TomID", "TombankID", "332233-7890");
    }
    //@author s174293 - Kasper Jørgensen
    @When("the {string} event is sent with that refund")
    public void theEventIsSentWithThatRefund(String arg0) {
        manager.logRefund(new Event(arg0, new Object[]{r, customer.getDtuPayID()}));
    }
    //@author s202772 - Gustav Kinch
    @Then("the log contains that refund with value of {bigdecimal}")
    public void theLogContainsThatRefund(BigDecimal amount) {
        Payment r_neg = new Payment("customerB", "merchantB", r.getAmount().negate());
        assertTrue(manager.getPayments().contains(r_neg));
        assertEquals(amount, r_neg.getAmount());
    }
}
