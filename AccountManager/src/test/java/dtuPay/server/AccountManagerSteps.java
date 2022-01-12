package dtuPay.server;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;



public class AccountManagerSteps {

    BankService bank = new BankServiceService().getBankServicePort();
    List<String> bankAccounts;

    @Before
    public void createBlankListOfCreatedAccounts() {
        bankAccounts = new ArrayList<String>();
    }

    @After
    public void deleteCreatedBankAccounts() {
        for (String account : bankAccounts) {
            try {
                bank.retireAccount(account);
            } catch (BankServiceException_Exception e) {
                e.printStackTrace();
            }
        }
    }


}
