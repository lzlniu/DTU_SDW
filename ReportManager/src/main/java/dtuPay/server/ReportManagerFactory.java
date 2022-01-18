package dtuPay.server;

import messaging.implementations.RabbitMqQueue;

public class ReportManagerFactory {
    static ReportManager paymentManager = null;
    //@author s213578 - Johannes Pedersen
    public ReportManager getManager() {
        if (paymentManager != null) {
            return paymentManager;
        }
        var mq = new RabbitMqQueue("rabbitMq");
        paymentManager = new ReportManager(mq);
        return paymentManager;
    }
}
