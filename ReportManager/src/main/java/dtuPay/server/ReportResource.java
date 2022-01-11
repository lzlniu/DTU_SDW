package dtuPay.server;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Reports")
public class ReportResource {
    ReportManager account = ReportManager.instance;

    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCustomerReport(Payment payment) {
        String newCid = null;
        try {
            return Response.ok().entity(newCid).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/merchants")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMerchantReport(Payment payment) {
        String newMid = null;
        try {
            return Response.ok().entity(newMid).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
