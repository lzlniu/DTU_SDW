package dtuPay.server;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import objects.Payment;

public class PaymentManager {
    public static PaymentManager instance = new PaymentManager();
    private BankService bank;

    public PaymentManager(){
        bank = new BankServiceService().getBankServicePort();
    }

    public boolean createPayment(Payment p) throws Exception {

        /*bank.transferMoneyFromTo(
                getUserById(customers,p.getCustomer()).getBankID(),
                getUserById(merchants,p.getMerchant()).getBankID(),
                p.getAmount(), "from: " + p.getCustomer() + " -> " + p.getMerchant());

        payments.add(p);*/
        return true;
    }
}
