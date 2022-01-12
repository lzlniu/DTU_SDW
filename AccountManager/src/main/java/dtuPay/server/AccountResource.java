package dtuPay.server;

import objects.DtuPayUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/accounts")
public class AccountResource {
    AccountManager account = new AccountManagerFactory().getManager();

    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DtuPayUser> getCustomers(){
        return account.getCustomers();
    }


    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(DtuPayUser customer) {
        String newCid = null;
        try {
            newCid = account.registerCustomer(customer);
            return Response.ok().entity(newCid).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/merchants")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMerchant(DtuPayUser merchant) {
        String newMid = null;
        try {
            newMid = account.registerMerchant(merchant);
            return Response.ok().entity(newMid).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/merchants")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DtuPayUser> getMerchants(){
        return account.getMerchants();
    }
}
