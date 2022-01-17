package dtuPay.server;

import messaging.implementations.RabbitMqQueue;

public class PaymentManagerFactory {

    static PaymentManager paymentManager = null;

    public PaymentManager getManager() {
        if (paymentManager != null) {
            return paymentManager;
        }
        var mq = new RabbitMqQueue("rabbitMq");
        paymentManager = new PaymentManager(mq);
        return paymentManager;
    }
}
