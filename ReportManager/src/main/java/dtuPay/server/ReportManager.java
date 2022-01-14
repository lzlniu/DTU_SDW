package dtuPay.server;

import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import objects.Payment;
import objects.DtuPayUser;

import java.util.*;

public class ReportManager {
    private MessageQueue mq;
    private HashMap<Payment, String> payments; //maps payments to customer ID's of the customer on that payment

    public ReportManager(MessageQueue mq) {
        this.mq = mq;
        payments = new HashMap<>();
        mq.addHandler("SuccessfulPayment",this::logPayment);
    }

    public Set<Payment> getCustomerPayments(String userID) {
        Set<Payment> report = new HashSet<>();
        for (Payment p : payments.keySet()){
            if (payments.get(p).equals(userID)) report.add(p);
        }
        return report;
    }

    public Set<Payment> getMerchantPayments(String userID) {
        Set<Payment> report = new HashSet<>();
        for (Payment p : payments.keySet()){
            if (p.getMerchantID().equals(userID)) report.add(p);
        }
        return report;
    }

    public Set<Payment> getPayments() {
        return payments.keySet();
    }

    public void logPayment(Event e) {
        var payment = e.getArgument(0,Payment.class);
        String customerID = e.getArgument(1,DtuPayUser.class).getDtuPayID();
        payments.put(payment, customerID);
    }
}
