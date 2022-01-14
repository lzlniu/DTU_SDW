package dtuPay.Token;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.MessageQueue;
import objects.DtuPayUser;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;

public class TokenManagerSteps {

    Exception e = new Exception();
    MessageQueue queue = mock(MessageQueue.class);
    TokenManager manager = new TokenManager(queue);
    List<String> tokens = new ArrayList<>();
    DtuPayUser customerA = new DtuPayUser();
    DtuPayUser customerB = new DtuPayUser();

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
    public void aListOfTokensIsRequested(int amount) {
        customerA.setFirstName("John");
        customerA.setLastName("Doe");
        customerA.setCPR("010170-0101");
        customerA.setBankID("10000");
        customerA.setDtuPayID("20000");
        try {
            manager.generateTokens(customerA, amount);
        } catch (Exception e) {
            this.e = e;
        }
    }

    @When("the customer request to generate {int} new tokens")
    public void theCustomerRequestToGenerateNewTokensList(int amount) {
        try {
            manager.generateTokens(customerA, amount);
        } catch (Exception e) {
            this.e = e;
        }
    }

    @Then("the customer have {int} tokens in the list")
    public void theCustomerHaveTokensInTheList(int amount) {
        assertEquals(amount, manager.getUserTokens(customerA).size());
    }

    @When("the TokenManager check whether the customer exist")
    public void theTokenManagerCheckWhetherTheCustomerExist() {
    }
}
