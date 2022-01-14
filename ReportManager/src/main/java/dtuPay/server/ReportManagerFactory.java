package dtuPay.server;

import messaging.implementations.RabbitMqQueue;

public class ReportManagerFactory {
    static ReportManager paymentManager = null;

    public ReportManager getManager() {
        if (paymentManager != null) {
            return paymentManager;
        }
        var mq = new RabbitMqQueue("localhost");
        paymentManager = new ReportManager(mq);
        return paymentManager;
    }
}
