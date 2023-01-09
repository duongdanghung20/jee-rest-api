package app;

import app.Assignment.Assignment;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("/gdd")
public class GddApplication {
    @Inject
    Gdd gdd;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public GddApplication() {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignmentList() {
        return Response.ok(this.gdd.obtainAssignmentList(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{searchtype}/{pattern}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSearch(@PathParam("searchtype") String searchType, @PathParam("pattern") String pattern) {
        if (searchType.equalsIgnoreCase("id")) {
            Assignment response = this.gdd.searchAssignmentById(Integer.parseInt(pattern));
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        else {
            if (searchType.equalsIgnoreCase("courseId")) {
                List<Assignment> response = this.gdd.searchAssignmentByCourseId(Integer.parseInt(pattern));
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
            }
            else if (searchType.equalsIgnoreCase("teacherId")) {
                List<Assignment> response = this.gdd.searchAssignmentByTeacherId(Integer.parseInt(pattern));
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
            }
            else {
                return Response.ok(new ArrayList<>(), MediaType.APPLICATION_JSON).build();
            }
        }
    }

    @GET
    @Path("/unassigned")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnassigned() {
        List<Assignment> responseList = this.gdd.searchUnassigned();
        return Response.ok(responseList, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces()
    public Response updateAssignment(@PathParam("id") int id,
                                  @FormParam("teacherId") int teacherId) {
        this.gdd.updateAssignmentById(id, teacherId);
        String respMsg = "Updated data of assignment ID " + id;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response deleteAssignment(@PathParam("id") int id) {
        this.gdd.deleteAssignment(id);
        String respMsg = "Deleted assignment ID " + id;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }
}
