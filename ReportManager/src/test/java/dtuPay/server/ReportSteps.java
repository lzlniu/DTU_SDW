package dtuPay.server;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import objects.DtuPayUser;
import objects.Payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReportSteps {
    MessageQueue queue = mock(MessageQueue.class);
    ReportManager manager = new ReportManager(queue);
    Payment p;
    DtuPayUser customer;

    //@author s202772 - Gustav Kinch
    @Given("a payment")
    public void aPayment(){
        p = new Payment("customer", "merchant", BigDecimal.valueOf(42));
        customer = new DtuPayUser("I", "Payed", "myID", "bankID", "123456-7890");
    }
    //@author s215949 - Zelin Li
    @When("the {string} event is sent with that payment")
    public void theEventIsSentWithThatPayment(String arg0) {
        manager.logPayment(new Event("SuccessfulPayment", new Object[]{p,customer.getDtuPayID()}));
    }
    //@author s213578 - Johannes Pedersen
    @Then("the log contains that payment")
    public void theLogContainsThatPayment() {
        assertTrue(manager.getPayments().contains(p));
    }
}
