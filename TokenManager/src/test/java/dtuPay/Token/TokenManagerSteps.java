package dtuPay.Token;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import objects.DtuPayUser;
import org.junit.vintage.engine.discovery.IsPotentialJUnit4TestClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenManagerSteps {

    Exception e = new Exception();
    MessageQueue queue = mock(MessageQueue.class);
    TokenManager manager = new TokenManager(queue);
    List<String> tokens = new ArrayList<>();
    List<String> tokens2 = new ArrayList<>();
    DtuPayUser customerA = new DtuPayUser();
    DtuPayUser customerB = new DtuPayUser();
    boolean customerAIsRegistered;
    boolean customerBIsRegistered;
    CompletableFuture<Boolean> responseReceived, responseReceived2;
    CompletableFuture<Boolean> eventReceived1, eventReceived2;
    UUID eventID1, eventID2;

    //@author s202772 - Gustav Kinch
    @Given("request {int} tokens")
    public void aCustomerRequestTokens(int amount) {
        try {
            tokens = manager.generateTokenList(amount);
        } catch (Exception e) {
            this.e = e;
        }
    }
    //@author s215949 - Zelin Li
    @Then("the tokens list is generated")
    public void theTokensListIsGenerated() {
        assertFalse(tokens.isEmpty());
    }

    //@author s213578 - Johannes Pedersen
    @Then("the tokens list is not generated")
    public void theTokensListIsNotGenerated() {
        assertTrue(tokens.isEmpty());
    }
    //@author s212643 - Xingguang Geng
    @Then("{int} unique tokens is returned")
    public void uniqueTokensIsReturned(int amount) {
        assertEquals(amount, tokens.size());
    }

    //@author s164422 - Thomas Bergen
    @Then("error message is {string}")
    public void errorMessageIs(String msg) {
        assertEquals(msg, e.getMessage());
    }
    //@author s174293 - Kasper Jørgensen
    @Given("a list of {int} tokens is requested by a customer")
    public void aListOfTokensIsRequested(int amount) throws ExecutionException, InterruptedException, TimeoutException {
        customerA.setFirstName("John");
        customerA.setLastName("Doe");
        customerA.setCPR("010170-0101");
        customerA.setBankID("10000");
        customerA.setDtuPayID("20000");
        responseReceived = new CompletableFuture<>();
        eventReceived1 = new CompletableFuture<>();
        new Thread(() -> {
            try {
                var tokens = manager.generateTokens(customerA, amount);
                responseReceived.complete(true);
            } catch (Exception e) {
                this.e = e;
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        manager.handleCustomerCanGetTokens(new Event("", new Object[]{eventID1, true}));
        responseReceived.get(10, TimeUnit.SECONDS);
    }
    //@author s202772 - Gustav Kinch
    @Given("two registered customers")
    public void twoRegisteredCustomers() {
        customerA.setFirstName("John");
        customerA.setLastName("Doe");
        customerA.setCPR("010170-0101");
        customerA.setBankID("10000");
        customerA.setDtuPayID("20000");
        customerB.setFirstName("Jane");
        customerB.setLastName("Doe");
        customerB.setCPR("010170-0102");
        customerB.setBankID("10001");
        customerB.setDtuPayID("20001");
        customerAIsRegistered = customerBIsRegistered = true;
    }
    //@author s202772 - Gustav Kinch
    @When("the customer request to generate {int} new tokens")
    public void theCustomerRequestToGenerateNewTokensList(int amount) throws InterruptedException {
        responseReceived = new CompletableFuture<>();
        new Thread(() -> {
            try {
                var tokens = manager.generateTokens(customerA, amount);
                responseReceived.complete(true);
            } catch (Exception e) {
                responseReceived.complete(false);
                this.e = e;
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
    }
    //@author s215949 - Zelin Li
    @Then("the customer have {int} tokens in the list")
    public void theCustomerHaveTokensInTheList(int amount) {
        assertEquals(amount, manager.getUserTokens(customerA).size());
    }
    //@author s213578 - Johannes Pedersen
    @When("the TokenManager check whether the customer exist")
    public void theTokenManagerCheckWhetherTheCustomerExist() {
    }
    //@author s212643 - Xingguang Geng
    @Given("the customer is registered")
    public void theCustomerIsRegistered() {
        customerAIsRegistered = true;
    }
    //@author s164422 - Thomas Bergen
    @Then("the {string} event is sent")
    public void theEventIsSent(String topic) {
        Event event = new Event(topic, new Object[]{customerA});
        verify(queue).publish(event);
    }
    //@author  s174293 - Kasper Jørgensen
    @When("the {string} event is recieved")
    public void theEventIsRecieved(String topic) {
        manager.handleCustomerCanGetTokens(new Event(topic, new Object[]{eventID1, customerAIsRegistered}));
    }
    //@author s202772 - Gustav Kinch
    @Then("response is received from manager")
    public void responseIsReceivedFromManager()  {
        try {
            var response = responseReceived.get(10, TimeUnit.SECONDS);
            assertNotNull(response);
        } catch (Exception ex) {
            fail();
        }

    }
    //@author s215949 - Zelin Li
    @And("the customer is not registered")
    public void theCustomerIsNotRegistered() {
        customerAIsRegistered = false;
    }

    //@author s202772 - Gustav Kinch
    @When("customer {int} requests to generate {int} new tokens")
    public void customerRequestsToGenerateNewTokens(int customer, int amount) throws InterruptedException {
                if (customer == 1){
            responseReceived = new CompletableFuture<>();
            eventReceived1 = new CompletableFuture<>();
            new Thread(() -> {
                try {
                    tokens = manager.generateTokens(customerA, amount);
                    responseReceived.complete(true);
                } catch (Exception e) {
                    responseReceived.complete(false);
                    this.e = e;
                }
            }).start();
        } else {
            responseReceived2 = new CompletableFuture<>();
            eventReceived2 = new CompletableFuture<>();
            new Thread(() -> {
                try {
                    tokens2 = manager.generateTokens(customerB, amount);
                    responseReceived2.complete(true);
                } catch (Exception e) {
                    responseReceived2.complete(false);
                    this.e = e;
                }
            }).start();
        }
        TimeUnit.SECONDS.sleep(1);
    }

    //@author s215949 - Zelin Li
    @When("the {string} event is received by customer {int}")
    public void theEventIsReceivedByCustomer(String topic, int customer) {
        if (customer == 1){
            manager.handleCustomerCanGetTokens(new Event(topic, new Object[]{eventID1, customerAIsRegistered}));
        } else {
            manager.handleCustomerCanGetTokens(new Event(topic, new Object[]{ eventID2, customerBIsRegistered}));
        }
    }

    //@author s213578 - Johannes Pedersen
    @Then("{int} unique tokens is returned to the first customer")
    public void uniqueTokensIsReturnedToTheFirstCustomer(int amount) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
        assertEquals(amount, tokens.size());
    }

    //@author s212643 - Xingguang Geng
    @And("{int} unique tokens is returned to the second customer")
    public void uniqueTokensIsReturnedToTheSecondCustomer(int amount) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
        assertEquals(amount, tokens2.size());
    }

    //@author s164422 - Thomas Bergen
    @Then("a {string} event is sent for customer {int}")
    public void aEventIsSentForCustomer(String arg0, int arg1) throws ExecutionException, InterruptedException, TimeoutException {
        if (arg1 == 1){
            assertTrue(eventReceived1.get(10, TimeUnit.SECONDS));
        } else {
            assertTrue(eventReceived2.get(10, TimeUnit.SECONDS));
        }
    }



    @Before
    public void aServiceHandlingCreatedRequests() {
        doAnswer(invocation -> {
            Event e = invocation.getArgument(0);
            var eventID = e.getArgument(0, UUID.class);
            var customerID = e.getArgument(1, DtuPayUser.class).getDtuPayID();
            if (customerID.equals(customerA.getDtuPayID())) {
                eventReceived1.complete(true);
                eventID1 = eventID;
            }
            else {
                eventReceived2.complete(true);
                eventID2 = eventID;
            }
            return null;
        }).when(queue).publish(any(Event.class));
    }
}
