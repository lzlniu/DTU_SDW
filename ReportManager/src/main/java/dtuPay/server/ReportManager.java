package dtuPay.server;

import messaging.implementations.RabbitMqQueue;
import objects.Payment;
import objects.DtuPayUser;
import java.util.ArrayList;
import java.util.List;

public class ReportManager {
    private RabbitMqQueue mq;
    private List<Payment> payments;

    public ReportManager(RabbitMqQueue mq) {
        this.mq = mq;
        payments = new ArrayList<>();
    }

    public List<Payment> getPayments(DtuPayUser user) {
        return null;
    }

    public List<Payment> getPayments() {
        return null;
    }

    public Payment logPayment(String token) {
        //get a specific payment record from its corresponding token
        return null;
    }
}
