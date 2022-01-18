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

    //@author s164422 - Thomas Bergen
    public ReportManager(MessageQueue mq) {
        this.mq = mq;
        payments = new HashMap<>();
        mq.addHandler("SuccessfulPayment",this::logPayment);
    }
    //@author s174293 - Kasper Jørgensen
    public Set<Payment> getCustomerPayments(String userID) {
        Set<Payment> report = new HashSet<>();
        for (Payment p : payments.keySet()){
            if (payments.get(p).equals(userID)) report.add(p);
        }
        return report;
    }
    //@author s202772 - Gustav Kinch
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
    //@author s215949 - Zelin Li
    public void logPayment(Event e) {
        var payment = e.getArgument(0,Payment.class);
        String customerID = e.getArgument(1,DtuPayUser.class).getDtuPayID();
        payments.put(payment, customerID);
    }
}
