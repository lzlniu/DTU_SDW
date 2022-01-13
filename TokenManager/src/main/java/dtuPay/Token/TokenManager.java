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
        boolean registered = isRegistered.join();
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
        return null;
    }

    public DtuPayUser getCustomerFromToken(){
        return null;
    }
}
