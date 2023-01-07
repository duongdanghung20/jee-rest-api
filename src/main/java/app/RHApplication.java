package app;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("/rh")
public class RHApplication {
    @Inject
    RH rh;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public RHApplication() {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeacherList() {
        return Response.ok(this.rh.obtainTeacherList(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{searchtype}/{pattern}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSearch(@PathParam("searchtype") String searchType, @PathParam("pattern") String pattern) {
        if (searchType.equalsIgnoreCase("id")) {
            Enseignant response = this.rh.searchTeacherById(Integer.parseInt(pattern));
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        else if (searchType.equalsIgnoreCase("name")){
            List<Enseignant> responseList = this.rh.searchTeacherByName(pattern);
            return Response.ok(responseList, MediaType.APPLICATION_JSON).build();
        }
        else {
            return Response.ok(new ArrayList<>(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces()
    public Response add(@FormParam("firstName") String firstName,
                        @FormParam("lastName") String lastName,
                        @FormParam("eq") float eq,
                        @FormParam("dept") String dept,
                        @FormParam("service") String service,
                        @FormParam("numDisHours") int numDisHours,
                        @FormParam("maxOvtHours") int maxOvtHours) {
        this.rh.addTeacher(firstName, lastName, eq, dept, service, numDisHours, maxOvtHours);
        String respMsg = "Added " + service + " " + firstName + " " + lastName + " of " + dept;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response updateTeacher(@PathParam("id") int id,
                                  @FormParam("firstName") String firstName,
                                  @FormParam("lastName") String lastName,
                                  @FormParam("eq") float eq,
                                  @FormParam("dept") String dept,
                                  @FormParam("service") String service,
                                  @FormParam("numDisHours") int numDisHours,
                                  @FormParam("maxOvtHours") int maxOvtHours) {
        this.rh.updateTeacherById(id, firstName, lastName, eq, dept, service, numDisHours, maxOvtHours);
        String respMsg = "Updated data of teacher ID " + id;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response deleteTeacher(@PathParam("id") int id) {
        this.rh.deleteTeacher(id);
        String respMsg = "Deleted teacher ID " + id;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }
}
