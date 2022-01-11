package dtuPay.Token;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Tokens")
public class TokenResource {


    @POST
    @Path("/Tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateTokens(DtuPayUser user) {
        /** Filled with w/e things that should be done different
         * */
        String newMid = null;
        try {
            return Response.ok().entity(newMid).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }


}
