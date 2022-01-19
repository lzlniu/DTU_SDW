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
    private Map<UUID, CompletableFuture<Boolean>> isRegistered;
    private Map<DtuPayUser,List<String>>activeTokens;

    //@author s164422 - Thomas Bergen
    public TokenManager(MessageQueue queue){
        activeTokens = new HashMap<>();
        isRegistered = new HashMap<>();
        this.mq = queue;
        mq.addHandler("CustomerRegisteredForTokens", this::handleCustomerCanGetTokens);
        mq.addHandler("GetCustomerFromToken", this::getCustomerFromToken);
        mq.addHandler("SuccessfulPayment", this::depleteToken);
    }

    //@author s174293 - Kasper Jørgensen
    private void depleteToken(Event e) {
        var payment = e.getArgument(0, Payment.class);
        var customer = e.getArgument(1, DtuPayUser.class);

        activeTokens.get(customer).remove(payment.getCustomerToken());
    }
    //@author s202772 - Gustav Kinch
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
    //@author s215949 - Zelin Li
    public void handleCustomerCanGetTokens(Event e) {
        var eventID = e.getArgument(0, UUID.class);
        var registered = e.getArgument(1, boolean.class);
        isRegistered.get(eventID).complete(registered);
    }
    //@author s213578 - Johannes Pedersen
    public List<String> generateTokens(DtuPayUser user, int n) throws Exception {
        UUID eventID = UUID.randomUUID();
        isRegistered.put(eventID, new CompletableFuture<>());
        Event event = new Event("GetTokensRequested", new Object[]{eventID, user});
        mq.publish(event);
        boolean registered = isRegistered.get(eventID).get(10, TimeUnit.SECONDS);

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
    //@author s212643 - Xingguang Geng
    public void getCustomerFromToken(Event e) {
        UUID eventID = e.getArgument(0, UUID.class);
        var customerToken = e.getArgument(1, String.class);

        for(DtuPayUser user : activeTokens.keySet()){
            var tokens = activeTokens.get(user);
            if(tokens.contains(customerToken)) {
                mq.publish(new Event("CustomerFromToken", new Object[]{eventID, true, user}));
            }
        }
        mq.publish(new Event("CustomerFromToken", new Object[]{eventID, false, null}));
    }
}
