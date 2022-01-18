package dtuPay.Token;

import objects.DtuPayUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tokens")
public class TokenResource {
    TokenManager manager = new TokenManagerFactory().getManager();
    //@author s174293 - Kasper Jørgensen
    @POST
    @Path("/{customer}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateTokens(@PathParam("customer") String n, DtuPayUser customer) {
        try {
            List<String> newTokens = manager.generateTokens(customer, Integer.valueOf(n));
            return Response.ok().entity(newTokens).build();
        } catch (Exception e) {
            System.out.println("anything");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
