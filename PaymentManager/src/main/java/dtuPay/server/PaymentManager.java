package dtuPay.server;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.implementations.RabbitMqQueue;
import objects.DtuPayUser;
import objects.Payment;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PaymentManager {
    private BankService bank;
    private RabbitMqQueue mq;
    private CompletableFuture<Boolean> customerFound;
    private CompletableFuture<Boolean> merchantFound;
    private DtuPayUser customer, merchant;


    public PaymentManager(RabbitMqQueue mq){
        this.mq = mq;
        bank = new BankServiceService().getBankServicePort();
        mq.addHandler("CustomerFromToken", this::handleCustomerFromToken);
        mq.addHandler("MerchantFromIDFound", this::handleMerchantFromIDFound);
    }

    private void handleMerchantFromIDFound(Event e) {
        boolean isMerchantFound = e.getArgument(0,boolean.class);

        if(isMerchantFound){
            this.merchant = e.getArgument(1, DtuPayUser.class);;
        }

        merchantFound.complete(isMerchantFound);
    }

    private void handleCustomerFromToken(Event e) {
        var isCustomerFound = e.getArgument(0, boolean.class);
        var dtuPayUser = e.getArgument(1, DtuPayUser.class);

        if(isCustomerFound){
            customer = dtuPayUser;
        }
        customerFound.complete(isCustomerFound);
    }

    public boolean createPayment(Payment p) throws Exception {
        customerFound = new CompletableFuture<>();
        mq.publish(new Event("GetCustomerFromToken", new Object[] {p.getCustomerToken()}));
        merchantFound = new CompletableFuture<>();
        mq.publish(new Event("GetMerchantFromID", new Object[]{p.getMerchantID()}));
        boolean customerIsFound = customerFound.get(10, TimeUnit.SECONDS);
        boolean merchantIsFound = merchantFound.get(10,TimeUnit.SECONDS);


        bank.transferMoneyFromTo(
                customer.getBankID(),
                merchant.getBankID(),
                p.getAmount(), merchant.getFirstName() + " " + merchant.getLastName() + " Received payment of" +
                        p.getAmount() + "kr.");

        return true;
    }
}
