package app;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import java.lang.Math;


@Transactional
@RequestScoped
public class Scolarite implements Serializable {
    @Inject
    Gdd gdd;
    Statement statement;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Scolarite() {};

    @PostConstruct
    public void init() throws SQLException {
        String urlDB = "jdbc:derby://localhost:1527/appDB;create=true";
        String username = "admin";
        String password = "admin";
        Connection con = DriverManager.getConnection(urlDB, username, password);
        statement = con.createStatement();

        // Create table ue if not exist
        try {
            String queryCreateUE =
                    "CREATE TABLE ue (" +
                            "id INTEGER GENERATED BY DEFAULT AS IDENTITY, " +
                            "name VARCHAR(255) NOT NULL, " +
                            "semester INTEGER NOT NULL, " +
                            "numCMHours DOUBLE NOT NULL, " +
                            "numTDHours DOUBLE NOT NULL, " +
                            "numTPHours DOUBLE NOT NULL, " +
                            "numStudents INTEGER NOT NULL, " +
                            "thresholdCM INTEGER NOT NULL, " +
                            "thresholdTD INTEGER NOT NULL, " +
                            "thresholdTP INTEGER NOT NULL, " +
                            "numCMGroups INTEGER NOT NULL, " +
                            "numTDGroups INTEGER NOT NULL, " +
                            "numTPGroups INTEGER NOT NULL, " +
                            "PRIMARY KEY (id)" +
                    ")";
            statement.execute(queryCreateUE);
        } catch (SQLException e) {
            if (e.getSQLState().equalsIgnoreCase("X0Y32")) {
                LOGGER.info("Ignore create table ue because already existed ##");
            }
            else {
                LOGGER.warning(e.getMessage());
            }
        }
        LOGGER.info("Creation");
    }

    public void addCourse(String name,
                          int semester,
                          double numCMHours,
                          double numTDHours,
                          double numTPHours,
                          int numStudents,
                          int thresholdCM,
                          int thresholdTD,
                          int thresholdTP) throws SQLException, URISyntaxException, IOException {

        UE course = new UE(name, semester, numCMHours, numTDHours, numTPHours,
                numStudents, thresholdCM, thresholdTD, thresholdTP);

        String queryAddUE = String.format(
                "INSERT INTO ue (name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP, numCMGroups, numTDGroups, numTPGroups) " +
                        "VALUES ('%s', %d, %f, %f, %f, %d, %d, %d, %d, %d, %d, %d)",
                        course.getName(),
                        course.getSemester(),
                        course.getNumCMHours(),
                        course.getNumTDHours(),
                        course.getNumTPHours(),
                        course.getNumStudents(),
                        course.getThresholdCM(),
                        course.getThresholdTD(),
                        course.getThresholdTP(),
                        course.getNumCMGroups(),
                        course.getNumTDGroups(),
                        course.getNumTPGroups()
        );
        statement.execute(queryAddUE);

        // Initialize course to the "assignment" table
        UE latestCourseAdded = searchLatestInsertedCourse();

        for (int i = 1; i <= latestCourseAdded.getNumCMGroups(); i++) {
            this.gdd.addAssignment(latestCourseAdded.getId(), -1, "CM", i, latestCourseAdded.getNumCMHours());
        }
        for (int i = 1; i <= latestCourseAdded.getNumTDGroups(); i++) {
            this.gdd.addAssignment(latestCourseAdded.getId(), -1, "TD", i, latestCourseAdded.getNumTDHours());
        }
        for (int i = 1; i <= latestCourseAdded.getNumTPGroups(); i++) {
            this.gdd.addAssignment(latestCourseAdded.getId(), -1, "TP", i, latestCourseAdded.getNumTPHours());
        }

        LOGGER.info("Added new course ####################################!");
    }

    public void updateCourseById(int id, String name, int semester, double numCMHours, double numTDHours, double numTPHours, int numStudents, int thresholdCM, int thresholdTD, int thresholdTP) {
        try {
            UE ueUpdate = searchCourseById(id).get(0);
            int prevNumCMGroups = ueUpdate.getNumCMGroups();
            int prevNumTDGroups = ueUpdate.getNumTDGroups();
            int prevNumTPGroups = ueUpdate.getNumTPGroups();

            int numCMGroups = (int) Math.ceil((float) numStudents / thresholdCM);
            int numTDGroups = (int) Math.ceil((float) numStudents / thresholdTD);
            int numTPGroups = (int) Math.ceil((float) numStudents / thresholdTP);

            String query = String.format(
                    "UPDATE ue SET name = '%s', semester = %d, numCMHours = %f, numTDHours = %f, numTPHours = %f, numStudents = %d, thresholdCM = %d, thresholdTD = %d, thresholdTP = %d, numCMGroups = %d, numTDGroups = %d, numTPGroups = %d " +
                            "WHERE id = %d", name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP, numCMGroups, numTDGroups, numTPGroups, id);
            statement.execute(query);

            int differenceCM = numCMGroups - prevNumCMGroups;
            int differenceTD = numTDGroups - prevNumTDGroups;
            int differenceTP = numTPGroups - prevNumTPGroups;

            if (differenceCM > 0) {
                for (int i = prevNumCMGroups + 1; i <= numCMGroups; i++) {
                    this.gdd.addAssignment(id, -1, "CM", i, ueUpdate.getNumCMHours());
                }
            }
            else if (differenceCM < 0) {
                List<Assignment> thisCourseAssignments = this.gdd.searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("CM") && assignment.getGroupNumber() > numCMGroups) {
                        this.gdd.deleteAssignment(assignment.getId());
                    }
                }
            }

            if (differenceTD > 0) {
                for (int i = prevNumTDGroups + 1; i <= numTDGroups; i++) {
                    this.gdd.addAssignment(id, -1, "TD", i, ueUpdate.getNumTDHours());
                }
            }
            else if (differenceTD < 0) {
                List<Assignment> thisCourseAssignments = this.gdd.searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("TD") && assignment.getGroupNumber() > numTDGroups) {
                        this.gdd.deleteAssignment(assignment.getId());
                    }
                }
            }

            if (differenceTP > 0) {
                for (int i = prevNumTPGroups + 1; i <= numTPGroups; i++) {
                    this.gdd.addAssignment(id, -1, "TP", i, ueUpdate.getNumTPHours());
                }
            }
            else if (differenceTP < 0) {
                List<Assignment> thisCourseAssignments = this.gdd.searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("TP") && assignment.getGroupNumber() > numTPGroups) {
                        this.gdd.deleteAssignment(assignment.getId());
                    }
                }
            }


        } catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCourse(int id) {
        try {
            String query = String.format("DELETE FROM ue WHERE id = %d", id);
            statement.execute(query);
            this.gdd.deleteAssignmentByCourseId(id);
        } catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UE> obtainCourseList() throws SQLException {
        String query = "SELECT * FROM ue";
        return getCourses(query);
    }

    public List<UE> searchCourseById(int id) throws NoResultException {
        try {
            String query = String.format("SELECT * FROM ue WHERE id = %d", id);
            return getCourses(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UE> searchCourseByName(String name) throws NoResultException {
        try {
            String query = String.format("SELECT * FROM ue WHERE LOWER(name) LIKE ('%%' || '%s' || '%%')", name.toLowerCase());
            return getCourses(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UE> searchCourseBySemester(int semester) throws NoResultException {
        try {
            String query = String.format("SELECT * FROM ue WHERE semester = %d", semester);
            return getCourses(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UE searchLatestInsertedCourse() throws NoResultException {
        try {
            String query = "SELECT * FROM ue WHERE id = ( SELECT MAX(id) FROM ue )";
            return getCourses(query).get(0);
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<UE> getCourses(String query) throws SQLException {
        ResultSet res = statement.executeQuery(query);
        ArrayList<UE> courseList = new ArrayList<UE>();
        while (res.next()) {
            UE course = new UE(
                    res.getString("name"),
                    res.getInt("semester"),
                    res.getDouble("numCMHours"),
                    res.getDouble("numTDHours"),
                    res.getDouble("numTPHours"),
                    res.getInt("numStudents"),
                    res.getInt("thresholdCM"),
                    res.getInt("thresholdTD"),
                    res.getInt("thresholdTP"));
            course.setId(res.getInt("id"));
            courseList.add(course);
        }
        return courseList;
    }
}