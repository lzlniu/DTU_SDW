package dtuPay.server;

import objects.Payment;
import objects.DtuPayUser;
import java.util.Set;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reports")
public class ReportResource {
    ReportManager report = new ReportManagerFactory().getManager();

    //@author s212643 - Xingguang Geng
    @GET
    @Path("/customers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getCustomerReport(@PathParam("id") String customer) throws Exception {
        return report.getCustomerPayments(customer);
    }

    //@author s164422 - Thomas Bergen
    @GET
    @Path("/merchants/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getMerchantReport(@PathParam("id") String merchant) throws Exception {
        return report.getMerchantPayments(merchant);
    }

    //@author s174293 - Kasper Jørgensen
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getSuperReport() throws Exception {
        return report.getPayments();
    }
}
