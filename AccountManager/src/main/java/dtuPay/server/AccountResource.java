package dtuPay.server;

import objects.DtuPayUser;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/accounts")
public class AccountResource {
    AccountManager account = new AccountManagerFactory().getManager();

    //@author s212643 - Xingguang Geng
    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DtuPayUser> getCustomers(){
        return account.getCustomers();
    }

    //@author s164422 - Thomas Bergen
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
    //@author s174293 - Kasper Jørgensen
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

    //@author s202772 - Gustav Kinch
    @GET
    @Path("/merchants")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DtuPayUser> getMerchants(){
        return account.getMerchants();
    }
}
