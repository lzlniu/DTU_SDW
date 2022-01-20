package dtuPay.server;

import objects.Payment;
import java.util.Set;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/reports")
public class ReportResource {
    ReportManager report = new ReportManagerFactory().getManager();

    //@author s212643 - Xingguang Geng
    @GET
    @Path("/customers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getCustomerReport(@PathParam("id") String cid) {
        return report.getCustomerPayments(cid);
    }

    //@author s164422 - Thomas Bergen
    @GET
    @Path("/merchants/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getMerchantReport(@PathParam("id") String mid) {
        return report.getMerchantPayments(mid);
    }

    //@author s174293 - Kasper Jørgensen
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getSuperReport() {
        return report.getPayments();
    }
}
