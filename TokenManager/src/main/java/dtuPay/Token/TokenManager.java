package dtuPay.Token;

import objects.DtuPayUser;
import messaging.Event;
import messaging.MessageQueue;


import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TokenManager {
    private MessageQueue queue;
    private CompletableFuture<Boolean> isRegistered;
    private Map<DtuPayUser,List<String>>activeTokens;
    private Map<DtuPayUser,List<String>>usedTokens;

    public TokenManager(MessageQueue queue){
        System.out.println("Im in token constructor!");
        activeTokens = new HashMap<>();
        usedTokens = new HashMap<>();
        this.queue = queue;
        queue.addHandler("CustomerRegisteredForTokens", this::handleCustomerCanGetTokens);
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
        queue.publish(event);
        System.out.println("event published");
        //boolean registered = isRegistered.join();
        if (! activeTokens.containsKey(user)) {
            System.out.println("activeTokens don't contains this user");
            activeTokens.put(user, generateTokenList(n));
            System.out.println("this user tokens generated and stored in activeTokens");
        }
        else if (activeTokens.get(user).size() <= 1 ) {
            System.out.println("activeTokens contains this user, but user have less or equal to 1 active token");
            activeTokens.get(user).addAll(generateTokenList(n));
            System.out.println("this user tokens generated and stored in activeTokens");
        } else {
            System.out.println("activeTokens contains this user");
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

    public DtuPayUser getCustomerFromToken(){
        return null;
    }
}
