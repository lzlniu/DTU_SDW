package dtuPay.Token;

import objects.DtuPayUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/Tokens")
public class TokenResource {
    TokenManager manager = new TokenManagerFactory().getManager();

    @POST
    @Path("/Tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateTokens(DtuPayUser user) {
        /** Filled with w/e things that should be done different
         * */
        try {
            List<String> newTokens = manager.generateTokens(user, 5);
            return Response.ok().entity(newTokens).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }


}
