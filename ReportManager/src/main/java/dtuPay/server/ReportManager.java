package dtuPay.server;

import messaging.Event;
import messaging.MessageQueue;
import objects.Payment;
import java.util.*;

public class ReportManager {
    private MessageQueue mq;
    private HashMap<Payment, String> payments; //maps payments to customer ID's of the customer on that payment

    //@author s164422 - Thomas Bergen
    public ReportManager(MessageQueue queue) {
        this.mq = queue;
        payments = new HashMap<>();
        mq.addHandler("SuccessfulPayment",this::logPayment);
    }
    //@author s174293 - Kasper Jørgensen
    protected Set<Payment> getCustomerPayments(String cid) {
        Set<Payment> report = new HashSet<>();
        for (Payment p : payments.keySet()){
            if (payments.get(p).equals(cid)) report.add(p);
        }
        return report;
    }
    //@author s202772 - Gustav Kinch
    protected Set<Payment> getMerchantPayments(String mid) {
        Set<Payment> report = new HashSet<>();
        for (Payment p : payments.keySet()){
            if (p.getMerchantID().equals(mid)) report.add(p);
        }
        return report;
    }

    protected Set<Payment> getPayments() {
        return payments.keySet();
    }

    //@author s215949 - Zelin Li
    protected void logPayment(Event e) {
        Payment p = e.getArgument(0,Payment.class);
        String cid = e.getArgument(1,String.class);
        payments.put(p, cid);
    }
}
