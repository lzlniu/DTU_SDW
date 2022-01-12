package dtuPay.Token;

import messaging.implementations.RabbitMqQueue;

public class TokenManagerFactory {
    static TokenManager tokenManager = null;

    public TokenManager getManager() {
        if (tokenManager != null) {
            return tokenManager;
        }
        var mq = new RabbitMqQueue("MessageQ");
        tokenManager = new TokenManager(mq);
        return tokenManager;
    }
}
