package dtuPay.Token;

import objects.DtuPayUser;
import messaging.Event;
import messaging.MessageQueue;
import objects.Payment;


import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TokenManager {
    private MessageQueue mq;
    private CompletableFuture<Boolean> isRegistered;
    private Map<DtuPayUser,List<String>>activeTokens;
    private Map<DtuPayUser,List<String>>usedTokens;

    public TokenManager(MessageQueue queue){
        activeTokens = new HashMap<>();
        usedTokens = new HashMap<>();
        this.mq = queue;
        mq.addHandler("CustomerRegisteredForTokens", this::handleCustomerCanGetTokens);
        mq.addHandler("GetCustomerFromToken", this::getCustomerFromToken);
        mq.addHandler("SuccessfulPayment", this::depleteToken);
    }

    private void depleteToken(Event e) {
        var payment = e.getArgument(0, Payment.class);
        var customer = e.getArgument(1, DtuPayUser.class);

        activeTokens.get(customer).remove(payment.getCustomerToken());

        if (!usedTokens.keySet().contains(customer)) usedTokens.put(customer, new ArrayList<>());
        usedTokens.get(customer).add(payment.getCustomerToken());
    }

    public List<String> generateTokenList(int n) throws Exception {
        List<String> returnList = new ArrayList<>();
        if (n < 1 || n > 5) {
            throw new Exception("Must create between 1 and 5 tokens");
        }

        for (int i = 0; i < n; i++) {
            returnList.add(UUID.randomUUID().toString());
        }
        return returnList;
    }

    public void handleCustomerCanGetTokens(Event e) {
        var registered = e.getArgument(0, boolean.class);
        isRegistered.complete(registered);
    }

    public List<String> generateTokens(DtuPayUser user, int n) throws Exception {
        isRegistered = new CompletableFuture<>();
        Event event = new Event("GetTokensRequested", new Object[]{user});
        mq.publish(event);
        boolean registered = isRegistered.get(10, TimeUnit.SECONDS);

        if (!registered) {throw new Exception("customer is not registered");}

        if (! activeTokens.containsKey(user)) {
            activeTokens.put(user, generateTokenList(n));
        }
        else if (activeTokens.get(user).size() <= 1 ) {
            activeTokens.get(user).addAll(generateTokenList(n));
        } else {
            throw new Exception("User already has more than 1 token");
        }
        return activeTokens.get(user);
    }

    public boolean depleteToken(String tokenID){
        return false;
    }

    public List<String> getUserTokens(DtuPayUser customer){
        return activeTokens.get(customer);
    } // Zelin Li

    public void getCustomerFromToken(Event e) {

        var customerToken = e.getArgument(0, String.class);

        for(DtuPayUser user : activeTokens.keySet()){
            var tokens = activeTokens.get(user);
            if(tokens.contains(customerToken)) {
                mq.publish(new Event("CustomerFromToken", new Object[]{true, user}));
            }
        }
        mq.publish(new Event("CustomerFromToken", new Object[]{false, null}));
    }
}
