package dtuPay.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/payments")
public class PaymentResource {
    PaymentManager pay = PaymentManager.instance;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPayment(Payment p) {
        try {
            pay.createPayment(p);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
