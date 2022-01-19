package dtuPay.server;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.MessageQueue;
import objects.DtuPayUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountManager {
    private MessageQueue queue;

    private List<DtuPayUser> customers;
    private List<DtuPayUser> merchants;

    private BankService bank;

    //@author s164422 - Thomas Bergen
    public AccountManager(MessageQueue queue) {
        this.queue = queue;
        customers = new ArrayList<>();
        merchants = new ArrayList<>();
        bank = new BankServiceService().getBankServicePort();
        setupHandlers();
    }
    //@author  s174293 - Kasper Jørgensen
    public void setupHandlers() {
        queue.addHandler("GetTokensRequested", this::handleTokensRequested);
        queue.addHandler("GetMerchantFromID", this::handleGetMerchantFromID);
    }
    //@author s202772 - Gustav Kinch
    private void handleGetMerchantFromID(Event e) {
        UUID eventID = e.getArgument(0, UUID.class);
        var merchantID = e.getArgument(1, String.class);
        try{
            DtuPayUser merchant = getUserById(merchants, merchantID);
            queue.publish(new Event("MerchantFromIDFound", new Object[]{eventID, true, merchant}));
        }catch (Exception exception){
            queue.publish(new Event("MerchantFromIDFound", new Object[]{eventID, false,null}));
        }
    }
    //@author s215949 - Zelin Li
    private void handleTokensRequested(Event event) {
        var eventID = event.getArgument(0, UUID.class);
        var customer = event.getArgument(1, DtuPayUser.class);
        boolean isRegistered = customers.contains(customer);
        Event response = new Event("CustomerRegisteredForTokens", new Object[]{eventID, isRegistered});
        queue.publish(response);
    }
    //@author s213578 - Johannes Pedersen
    public String registerCustomer(DtuPayUser customer) throws Exception {
        return createDTUPayUser(customers,customer);
    }
    //@author s212643 - Xingguang Geng
    public String registerMerchant(DtuPayUser merchant) throws Exception {
        return createDTUPayUser(merchants,merchant);
    }
    //@author s164422 - Thomas Bergen
    public String createDTUPayUser(List<DtuPayUser> list, DtuPayUser user) throws Exception {
        try {
            bank.getAccount(user.getBankID());
            user.setDtuPayID(genID());
            list.add(user);
            return user.getDtuPayID();
        } catch (BankServiceException_Exception e) {
            throw new Exception("must have a bank account to register");
        }
    }
    //@author s174293 - Kasper Jørgensen
    public String genID(){
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (!idChecker(id));
        return id;
    }
    //@author s202772 - Gustav Kinch
    public Boolean idChecker(String id){
        for (DtuPayUser customer : customers) {
            if (customer.getDtuPayID().equals(id)){
                return false;
            }
        }
        for (DtuPayUser merchant : merchants) {
            if (merchant.getDtuPayID().equals(id)){
                return false;
            }
        }
        return true;
    }

    public List<DtuPayUser> getCustomers() {
        return customers;
    }

    public List<DtuPayUser> getMerchants() {
        return merchants;
    }

    //@author s215949 - Zelin Li
    public DtuPayUser getUserById(List<DtuPayUser> list, String id) throws Exception {
        for (DtuPayUser dtuPayUser : list) {
            if (dtuPayUser.getDtuPayID().equals(id)){
                return dtuPayUser;
            }
        }
        if(list == customers){
            throw new Exception("customer id is unknown");
        } else {
            throw new Exception("merchant id is unknown");
        }
    }
}
