package dtuPay.server;

import messaging.implementations.RabbitMqQueue;

public class AccountManagerFactory {

        static AccountManager accountManager = null;

        public AccountManager getManager() {
            if (accountManager != null) {
                return accountManager;
            }

            var mq = new RabbitMqQueue("localhost");

            accountManager = new AccountManager(mq);
            return accountManager;
        }
}
