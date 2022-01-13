package dtuPay.Token;

import messaging.implementations.RabbitMqQueue;

public class TokenManagerFactory {
    static TokenManager tokenManager = null;

    public TokenManager getManager() {
        System.out.println("In tokenmanagerfactory constructor");
        if (tokenManager != null) {
            return tokenManager;
        }
        var mq = new RabbitMqQueue("localhost");

        tokenManager = new TokenManager(mq);
        System.out.println("called tokenmanager constructor");
        return tokenManager;
    }
}
