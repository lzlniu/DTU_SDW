package dtuPay.server;

import messaging.implementations.RabbitMqQueue;

public class AccountManagerFactory {
    static AccountManager accountManager = null;
    //@author s213578 - Johannes Pedersen
    public AccountManager getManager() {
        if (accountManager != null) {
            return accountManager;
        }
        RabbitMqQueue mq = new RabbitMqQueue("rabbitMq");
        accountManager = new AccountManager(mq);
        return accountManager;
    }
}
