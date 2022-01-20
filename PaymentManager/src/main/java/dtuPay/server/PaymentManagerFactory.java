package dtuPay.server;

import messaging.implementations.RabbitMqQueue;

public class PaymentManagerFactory {
    static PaymentManager paymentManager = null;
    //@author s213578 - Johannes Pedersen
    public PaymentManager getManager() {
        if (paymentManager != null) {
            return paymentManager;
        }
        RabbitMqQueue mq = new RabbitMqQueue("rabbitMq");
        paymentManager = new PaymentManager(mq);
        return paymentManager;
    }
}
