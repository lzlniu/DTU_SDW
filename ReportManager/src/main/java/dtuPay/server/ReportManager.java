package dtuPay.server;

import objects.Payment;
import objects.DtuPayUser;
import java.util.ArrayList;
import java.util.List;

public class ReportManager {
    public static ReportManager instance = new ReportManager();
    private List<Payment> payments;

    public ReportManager() {
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
