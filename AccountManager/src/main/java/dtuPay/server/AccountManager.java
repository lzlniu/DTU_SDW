package dtuPay.server;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountManager {
    public static AccountManager instance = new AccountManager();
    private List<DtuPayUser> customers;
    private List<DtuPayUser> merchants;

    private BankService bank;

    public AccountManager() {
        customers = new ArrayList<>();
        merchants = new ArrayList<>();
        bank = new BankServiceService().getBankServicePort();
    }

    public String registerCustomer(DtuPayUser customer) throws Exception {
        return createDTUPayUser(customers,customer);
    }

    public String registerMerchant(DtuPayUser merchant) throws Exception {
        return createDTUPayUser(merchants,merchant);
    }

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

    public String genID(){
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (!idChecker(id));
        return id;
    }

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