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
    private MessageQueue mq;
    private List<DtuPayUser> customers;
    private List<DtuPayUser> merchants;
    private BankService bank;

    //@author s164422 - Thomas Bergen
    public AccountManager(MessageQueue queue) {
        this.mq = queue;
        customers = new ArrayList<>();
        merchants = new ArrayList<>();
        bank = new BankServiceService().getBankServicePort();
        setupHandlers();
    }

    //@author  s174293 - Kasper Jørgensen
    private void setupHandlers() {
        mq.addHandler("GetTokensRequested", this::handleTokensRequested);
        mq.addHandler("GetMerchantFromID", this::handleGetMerchantFromID);
        mq.addHandler("CustomerFromToken", this::handleCustomerFromToken);
    }

    private void handleCustomerFromToken(Event e)  {
        UUID eventID = e.getArgument(0, UUID.class);
        boolean activeToken = e.getArgument(1, boolean.class);
        DtuPayUser customer = null;
        if (activeToken) {
            try {
                customer = getUserById(customers, e.getArgument(2, String.class));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        mq.publish(new Event("CustomerInformationFromToken", new Object[]{eventID, activeToken, customer}));
    }

    //@author s202772 - Gustav Kinch
    private void handleGetMerchantFromID(Event e) {
        UUID eventID = e.getArgument(0, UUID.class);
        String mid = e.getArgument(1, String.class);
        try {
            DtuPayUser merchant = getUserById(merchants, mid);
            mq.publish(new Event("MerchantFromIDFound", new Object[]{eventID, true, merchant}));
        } catch (Exception exception){
            mq.publish(new Event("MerchantFromIDFound", new Object[]{eventID, false,null}));
        }
    }

    //@author s215949 - Zelin Li
    private void handleTokensRequested(Event event) {
        UUID eventID = event.getArgument(0, UUID.class);
        String cid = event.getArgument(1, String.class);
        boolean isRegistered = false;
        for (DtuPayUser customer : customers) {
            if (customer.getDtuPayID().equals(cid)){
                isRegistered = true;
                break;
            }
        }
        mq.publish(new Event("CustomerRegisteredForTokens", new Object[]{eventID, isRegistered}));
    }

    //@author s213578 - Johannes Pedersen
    protected String registerCustomer(DtuPayUser customer) throws Exception {
        return createDTUPayUser(customers,customer);
    }
    //@author s212643 - Xingguang Geng
    protected String registerMerchant(DtuPayUser merchant) throws Exception {
        return createDTUPayUser(merchants,merchant);
    }
    //@author s164422 - Thomas Bergen
    private String createDTUPayUser(List<DtuPayUser> list, DtuPayUser user) throws Exception {
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
    private String genID(){
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (!idChecker(id));
        return id;
    }

    //@author s202772 - Gustav Kinch
    private boolean idChecker(String id) {
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

    protected List<DtuPayUser> getCustomers() {
        return customers;
    }

    protected List<DtuPayUser> getMerchants() {
        return merchants;
    }

    //@author s215949 - Zelin Li
    private DtuPayUser getUserById(List<DtuPayUser> list, String id) throws Exception {
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

    public void deleteCustomer(String cid) throws Exception {
        customers.remove(getUserById(customers, cid));
    }

    public void deleteMerchant(String mid) throws Exception {
        merchants.remove(getUserById(merchants, mid));
    }
}
