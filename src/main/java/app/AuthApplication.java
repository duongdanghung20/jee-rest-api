package app;

import app.Acc.Acc;

import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/auth")
public class AuthApplication {
    @Inject
    Auth auth;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public AuthApplication() {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountList() {
        return Response.ok(this.auth.obtainAccountList(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{pattern}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchAccountByUsername(@PathParam("pattern") String pattern) {
        Acc response = this.auth.searchAccountByUsername(pattern);
        if (response == null) {
            return Response.ok(new Acc("NoSuchUser", "NoSuchUser", "NoSuchUser"), MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces()
    public Response add(@FormParam("username") String username,
                        @FormParam("password") String password,
                        @FormParam("role") String role) {
        this.auth.addAccount(username, password, role);
        String respMsg = "Added account new account " + username;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }

    @DELETE
    @Path("/{username}")
    @Produces()
    public Response deleteAccount(@PathParam("username") String username) {
        this.auth.deleteAccount(username);
        String respMsg = "Deleted account " + username;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }
}
