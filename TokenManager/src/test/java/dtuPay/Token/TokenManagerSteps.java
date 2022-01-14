package dtuPay.Token;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import objects.DtuPayUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TokenManagerSteps {

    Exception e = new Exception();
    MessageQueue queue = mock(MessageQueue.class);
    TokenManager manager = new TokenManager(queue);
    List<String> tokens = new ArrayList<>();
    DtuPayUser customerA = new DtuPayUser();
    DtuPayUser customerB = new DtuPayUser();
    boolean customerIsRegistered;
    CompletableFuture<Boolean> responseRecieved;

    @Given("request {int} tokens")
    public void aCustomerRequestTokens(int amount) {
        try {
            tokens = manager.generateTokenList(amount);
        } catch (Exception e) {
            this.e = e;
        }
    }

    @Then("the tokens list is generated")
    public void theTokensListIsGenerated() {
        assertFalse(tokens.isEmpty());
    }

    @Then("the tokens list is not generated")
    public void theTokensListIsNotGenerated() {
        assertTrue(tokens.isEmpty());
    }

    @Then("{int} unique tokens is returned")
    public void uniqueTokensIsReturned(int amount) {
        assertEquals(amount, tokens.size());
    }

    @Then("error message is {string}")
    public void errorMessageIs(String msg) {
        assertEquals(msg, e.getMessage());
    }

    @Given("a list of {int} tokens is requested by a customer")
    public void aListOfTokensIsRequested(int amount) throws ExecutionException, InterruptedException, TimeoutException {
        customerA.setFirstName("John");
        customerA.setLastName("Doe");
        customerA.setCPR("010170-0101");
        customerA.setBankID("10000");
        customerA.setDtuPayID("20000");
        responseRecieved = new CompletableFuture<>();
        new Thread(() -> {
            try {
                var tokens = manager.generateTokens(customerA, amount);
                responseRecieved.complete(true);
            } catch (Exception e) {
                this.e = e;
            }
        }).start();
        manager.handleCustomerCanGetTokens(new Event("", new Object[]{true}));
        responseRecieved.get(10, TimeUnit.SECONDS);
    }

    @When("the customer request to generate {int} new tokens")
    public void theCustomerRequestToGenerateNewTokensList(int amount){
        responseRecieved = new CompletableFuture<>();
        new Thread(() -> {
            try {
                var tokens = manager.generateTokens(customerA, amount);
                responseRecieved.complete(true);
            } catch (Exception e) {
                responseRecieved.complete(false);
                this.e = e;
            }
        }).start();
    }

    @Then("the customer have {int} tokens in the list")
    public void theCustomerHaveTokensInTheList(int amount) {
        assertEquals(amount, manager.getUserTokens(customerA).size());
    }

    @When("the TokenManager check whether the customer exist")
    public void theTokenManagerCheckWhetherTheCustomerExist() {
    }

    @Given("the customer is registered")
    public void theCustomerIsRegistered() {
        customerIsRegistered = true;
    }

    @Then("the {string} event is sent")
    public void theEventIsSent(String topic) {
        Event event = new Event(topic, new Object[]{customerA});
        verify(queue).publish(event);
    }

    @When("the {string} event is recieved")
    public void theEventIsRecieved(String topic) {
        manager.handleCustomerCanGetTokens(new Event(topic, new Object[]{customerIsRegistered}));
    }

    @Then("response is received from manager")
    public void responseIsReceivedFromManager()  {
        try {
            var response = responseRecieved.get(10, TimeUnit.SECONDS);
            assertNotNull(response);
        } catch (Exception ex) {
            fail();
        }

    }

    @And("the customer is not registered")
    public void theCustomerIsNotRegistered() {
        customerIsRegistered = false;
    }
}
