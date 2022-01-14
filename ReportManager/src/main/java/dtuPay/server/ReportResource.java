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

    @GET
    @Path("/customers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getCustomerReport(@PathParam("id") String customer) throws Exception {
        return report.getCustomerPayments(customer);
    }

    @GET
    @Path("/merchants/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getMerchantReport(@PathParam("id") String merchant) throws Exception {
        return report.getMerchantPayments(merchant);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Payment> getSuperReport() throws Exception {
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
