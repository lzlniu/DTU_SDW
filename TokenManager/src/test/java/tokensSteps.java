import dtuPay.Token.TokenManager;
import dtuPay.Token.TokenManagerFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.MessageQueue;
import objects.DtuPayUser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class tokensSteps {

    Exception e = new Exception();
    MessageQueue queue = mock(MessageQueue.class);
    TokenManager manager = new TokenManager(queue);
    List<String> tokens = new ArrayList<>();

    @Given("a customer request {int} tokens")
    public void aCustomerRequestTokens(int amount) throws Exception {
        tokens = manager.generateTokenList(amount);
    }

    @When("the tokens list is generated")
    public boolean theTokensListIsGenerated() {
        if(!tokens.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Then("{int} unique tokens is returned")
    public void uniqueTokensIsReturned(int amount) {
        assertEquals(tokens.size(), amount);
    }

    @Then("error with message {string}")
    public void errorWithMessage(String arg0) {
    }

    @Then("error message is {string}")
    public void errorMessageIs(String arg0) {
    }

    @Given("a list of {int} tokens is requested")
    public void aListOfTokensIsRequested(int arg0) {
    }

    @When("the TokenManager check whether the customer exist")
    public void theTokenManagerCheckWhetherTheCustomerExist() {
    }

    @Given("a customer with {int} tokens")
    public void aCustomerWithTokens(int arg0) {
    }

    @When("the customer request to generate new tokens list")
    public void theCustomerRequestToGenerateNewTokensList() {
    }
}
