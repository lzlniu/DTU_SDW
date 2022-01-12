package dtuPay.server;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import objects.DtuPayUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class AccountManagerSteps {

    DtuPayUser customer = new DtuPayUser();
    DtuPayUser merchant = new DtuPayUser();
    AccountManager manager = AccountManager.instance;
    BankService bank = new BankServiceService().getBankServicePort();
    Exception e = new Exception();
    List<String> bankAccounts;

    @Before
    public void create_blank_list_of_created_accounts() {
        bankAccounts = new ArrayList<String>();
    }

    @After
    public void delete_created_bank_accounts() {
        for (String account : bankAccounts) {
            try {
                bank.retireAccount(account);
            } catch (BankServiceException_Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Given("a customer with a bank account with balance")
    public void a_customer_with_a_bank_account_with_balance() throws BankServiceException_Exception {
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCPR("010170-0101");
        User user = new User();
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());
        user.setCprNumber(customer.getCPR());
        customer.setBankID(bank.createAccountWithBalance(user, BigDecimal.valueOf(9204.52)));
        bankAccounts.add(customer.getBankID());
    }

    @When("the customer registers with DTU Pay")
    public void the_customer_registers_with_dtu_pay() throws Exception {
        try {
            customer.setDtuPayID(manager.registerCustomer(customer));
        } catch (Exception e) {
            this.e = e;
        }
    }

    @Then("that customer is registered with DTU Pay")
    public void that_customer_is_registered_with_dtu_pay() {
        List<DtuPayUser> list = manager.getCustomers();
        assertTrue(list.contains(customer));
    }

    @Given("a merchant with a bank account with balance")
    public void a_merchant_with_a_bank_account_with_balance() throws BankServiceException_Exception {
        merchant.setFirstName("Jane");
        merchant.setLastName("Doe");
        merchant.setCPR("020280-0202");
        User user = new User();
        user.setFirstName(merchant.getFirstName());
        user.setLastName(merchant.getLastName());
        user.setCprNumber(merchant.getCPR());
        merchant.setBankID(bank.createAccountWithBalance(user, BigDecimal.valueOf(5404.52)));
        bankAccounts.add(merchant.getBankID());
    }

    @When("the merchant registers with DTU Pay")
    public void the_merchant_registers_with_dtu_pay() throws Exception {
        try {
            merchant.setDtuPayID(manager.registerMerchant(merchant));
        } catch (Exception e) {
            this.e = e;
        }
    }

    @Then("that merchant is registered with DTU Pay")
    public void that_merchant_is_registered_with_dtu_pay() {
        List<DtuPayUser> list = manager.getMerchants();
        assertTrue(list.contains(merchant));
    }

    @Given("a customer with no bank account")
    public void a_customer_with_no_bank_account() {
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCPR("010170-0101");
    }

    @Given("a merchant with no bank account")
    public void a_merchant_with_no_bank_account() {
        merchant.setFirstName("Jane");
        merchant.setLastName("Doe");
        merchant.setCPR("020280-0202");
    }

    @Then("an error message is returned saying {string}")
    public void an_error_message_is_returned_saying(String string) {
        assertEquals(string, e.getMessage());
    }
}
