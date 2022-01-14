package dtuPay.server;

import objects.Payment;
import objects.DtuPayUser;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reports")
public class ReportResource {
    ReportManager report = new ReportManagerFactory().getManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> getCustomerReport(DtuPayUser customer) throws Exception {
        return report.getPayments(customer);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> getMerchantReport(DtuPayUser merchant) throws Exception {
        return report.getPayments(merchant);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> getSuperReport() throws Exception {
        return report.getPayments();
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response getCustomerReport(DtuPayUser customer) {
//        try {
//            report.getPayments(customer);
//            return Response.ok().build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
//        }
//    }
//
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response getMerchantReport(DtuPayUser merchant) {
//        try {
//            report.getPayments(merchant);
//            return Response.ok().build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
//        }
//    }
//
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response getSuperReport() {
//        try {
//            report.getPayments();
//            return Response.ok().build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
//        }
//    }
}
