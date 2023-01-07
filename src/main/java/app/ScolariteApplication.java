package app;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("/scolarite")
public class ScolariteApplication {
    @Inject
    Scolarite scolarite;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public ScolariteApplication() {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseList() {
        return Response.ok(this.scolarite.obtainCourseList(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{searchtype}/{pattern}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSearch(@PathParam("searchtype") String searchType, @PathParam("pattern") String pattern) {
        if (searchType.equalsIgnoreCase("id")) {
            UE response = this.scolarite.searchCourseById(Integer.parseInt(pattern));
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        else {
            List<UE> responseList = switch (searchType) {
                case "name" -> this.scolarite.searchCourseByName(pattern);
                case "semester" -> this.scolarite.searchCourseBySemester(Integer.parseInt(pattern));
                default -> new ArrayList<>();
            };
            return Response.ok(responseList, MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces()
    public Response add(@FormParam("name") String name,
                        @FormParam("semester") int semester,
                        @FormParam("numCMHours") double numCMHours,
                        @FormParam("numTDHours") double numTDHours,
                        @FormParam("numTPHours") double numTPHours,
                        @FormParam("numStudents") int numStudents,
                        @FormParam("thresholdCM") int thresholdCM,
                        @FormParam("thresholdTD") int thresholdTD,
                        @FormParam("thresholdTP") int thresholdTP) {
        this.scolarite.addCourse(name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP);
        String respMsg = "Added course " + name + " of semester " + semester;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces()
    public Response updateTeacher(@PathParam("id") int id,
                                  @FormParam("name") String name,
                                  @FormParam("semester") int semester,
                                  @FormParam("numCMHours") double numCMHours,
                                  @FormParam("numTDHours") double numTDHours,
                                  @FormParam("numTPHours") double numTPHours,
                                  @FormParam("numStudents") int numStudents,
                                  @FormParam("thresholdCM") int thresholdCM,
                                  @FormParam("thresholdTD") int thresholdTD,
                                  @FormParam("thresholdTP") int thresholdTP) {
        this.scolarite.updateCourseById(id, name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP);
        String respMsg = "Updated data of course ID " + id;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces()
    public Response deleteBook(@PathParam("id") int id) {
        this.scolarite.deleteCourse(id);
        String respMsg = "Deleted course ID " + id;
        LOGGER.info(respMsg);
        return Response.ok(respMsg).build();
    }
}
