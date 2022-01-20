package dtuPay.Token;

import messaging.implementations.RabbitMqQueue;

public class TokenManagerFactory {
    static TokenManager tokenManager = null;
    //@author s164422 - Thomas Bergen
    public TokenManager getManager() {
        if (tokenManager != null) {
            return tokenManager;
        }
        RabbitMqQueue mq = new RabbitMqQueue("rabbitMq");
        tokenManager = new TokenManager(mq);
        return tokenManager;
    }
}
