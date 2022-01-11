package dtuPay.server;

import java.util.List;
import objects.Payment

public class ReportManager {
    public static ReportManager instance = new ReportManager();

    public ReportManager() {
        payments = new List<Payment>;
    }

    public List<Payment> getPayments() throws Exception {
        return payments;
    }

    public void logPayments(Payment p) throws Exception {
        ;
    }
}
