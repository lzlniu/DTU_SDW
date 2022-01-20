package dtuPay.server;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.MessageQueue;
import objects.DtuPayUser;
import objects.Payment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PaymentManager {
    private BankService bank;
    private MessageQueue mq;
    private Map<UUID, CompletableFuture<Boolean>> customerFound = new HashMap<>();
    private Map<UUID, CompletableFuture<Boolean>> merchantFound = new HashMap<>();
    private Map<UUID, DtuPayUser> customer = new HashMap<>();
    private Map<UUID, DtuPayUser> merchant = new HashMap<>();

    //@author s164422 - Thomas Bergen
    public PaymentManager(MessageQueue queue){
        this.mq = queue;
        bank = new BankServiceService().getBankServicePort();
        mq.addHandler("CustomerInformationFromToken", this::handleCustomerInformationFromToken);
        mq.addHandler("MerchantFromIDFound", this::handleMerchantFromIDFound);
    }

    //@author s174293 - Kasper Jørgensen
    private void handleMerchantFromIDFound(Event e) {
        UUID eventID = e.getArgument(0, UUID.class);
        boolean isMerchantFound = e.getArgument(1,boolean.class);
        if(isMerchantFound) this.merchant.put(eventID, e.getArgument(2, DtuPayUser.class));
        merchantFound.get(eventID).complete(isMerchantFound);
    }

    //@author s202772 - Gustav Kinch
    private void handleCustomerInformationFromToken(Event e) {
        UUID eventID = e.getArgument(0, UUID.class);
        boolean isCustomerFound = e.getArgument(1, boolean.class);
        if(isCustomerFound) {
            this.customer.put(eventID, e.getArgument(2, DtuPayUser.class));
        }
        customerFound.get(eventID).complete(isCustomerFound);
    }

    //@author s202772 - Gustav Kinch
    private void getAndValidatePaymentInfo(UUID eventID, Payment p) throws Exception {
        customerFound.put(eventID, new CompletableFuture<>());
        mq.publish(new Event("GetCustomerFromToken", new Object[] {eventID, p.getCustomerToken()}));
        merchantFound.put(eventID, new CompletableFuture<>());
        mq.publish(new Event("GetMerchantFromID", new Object[]{eventID, p.getMerchantID()}));
        boolean customerIsFound = customerFound.get(eventID).get(10, TimeUnit.SECONDS);
        boolean merchantIsFound = merchantFound.get(eventID).get(10,TimeUnit.SECONDS);
        if (!customerIsFound) throw new Exception("Customer token not valid");
        if (!merchantIsFound) throw new Exception("merchant id is unknown");
    }

    //@author s215949 - Zelin Li
    protected void createPayment(Payment p) throws Exception {
        mq.publish(new Event("SuccessfulPayment", new Object[]{
                p, customer
                .get(bankTransfer(customer, merchant, p))
                .getDtuPayID()
        }));
    }

    protected void createRefund(Payment p) throws Exception {
        mq.publish(new Event("SuccessfulRefund", new Object[]{
                p, customer
                .get(bankTransfer(merchant, customer, p))
                .getDtuPayID()
        }));
    }

    protected UUID bankTransfer(Map<UUID, DtuPayUser> whoTransfer, Map<UUID, DtuPayUser> whoReceived, Payment p) throws Exception {
        UUID eventID = UUID.randomUUID();
        getAndValidatePaymentInfo(eventID, p);
        bank.transferMoneyFromTo(
                whoTransfer.get(eventID).getBankID(),
                whoReceived.get(eventID).getBankID(),
                p.getAmount(), whoReceived.get(eventID).getFirstName()
                        + " " + whoReceived.get(eventID).getLastName()
                        + " Received a money for " + p.getAmount() + " kr."
        );
        return eventID;
    }
}
