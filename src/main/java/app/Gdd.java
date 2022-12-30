package app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Transactional
@RequestScoped
public class Gdd implements Serializable {
    Statement statement;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Gdd() {};

    @PostConstruct
    public void init() throws SQLException {
        String urlDB = "jdbc:derby://localhost:1527/appDB;create=true";
        String username = "admin";
        String password = "admin";
        Connection con = DriverManager.getConnection(urlDB, username, password);
        statement = con.createStatement();

        // Create table assignment if not exist
        try {
            String queryCreateAssignment =
                    "CREATE TABLE assignment (" +
                            "id INTEGER GENERATED BY DEFAULT AS IDENTITY, " +
                            "courseId INTEGER NOT NULL, " +
                            "teacherId INTEGER NOT NULL, " +
                            "groupType VARCHAR(255) NOT NULL, " +
                            "groupNumber INTEGER NOT NULL, " +
                            "numHours DOUBLE NOT NULL, " +
                            "PRIMARY KEY (id)" +
                    ")";
            statement.execute(queryCreateAssignment);
        } catch (SQLException e) {
            if (e.getSQLState().equalsIgnoreCase("X0Y32")) {
                LOGGER.info("Ignore create table assignment because already existed ##");
            }
            else {
                LOGGER.warning(e.getMessage());
            }
        }
        LOGGER.info("Creation!!");
    }

    public void addAssignment(int courseId, int teacherId, String groupType, int groupNumber, double numHours) throws SQLException {

        Assignment a = new Assignment(courseId, teacherId, groupType, groupNumber, numHours);

        String queryAddAssignment = String.format(
                "INSERT INTO assignment (courseId, teacherId, groupType, groupNumber, numHours) " +
                        "VALUES (%d, %d, '%s', %d, %f)",
                a.getCourseId(),
                a.getTeacherId(),
                a.getGroupType(),
                a.getGroupNumber(),
                a.getNumHours()
        );
        statement.execute(queryAddAssignment);
        LOGGER.info("Added new assignment ####################################!");
    }

    public void updateAssignmentById(int id, int teacherId) {
        try {
            String query = String.format(
                    "UPDATE assignment SET teacherId = %d WHERE id = %d", teacherId, id);
            statement.execute(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAssignment(int id) {
        try {
            String query = String.format("DELETE FROM assignment WHERE id = %d", id);
            statement.execute(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAssignmentByCourseId(int courseId) {
        try {
            String query = String.format("DELETE FROM assignment WHERE courseId = %d", courseId);
            statement.execute(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Assignment> obtainAssignmentList() throws SQLException {
        String query = "SELECT * FROM assignment";
        return getAssignments(query);
    }

    public List<Assignment> searchAssignmentById(int id) throws NoResultException {
        try {
            String query = String.format("SELECT * FROM assignment WHERE id = %d", id);
            return getAssignments(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Assignment> searchAssignmentByCourseId(int courseId) throws NoResultException {
        try {
            String query = String.format("SELECT * FROM assignment WHERE courseId = %d", courseId);
            return getAssignments(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Assignment> searchAssignmentByTeacherId(int teacherId) throws NoResultException {
        try {
            String query = String.format("SELECT * FROM assignment WHERE teacherId = %d", teacherId);
            return getAssignments(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Assignment> searchUnassigned() throws NoResultException {
        try {
            String query = "SELECT * FROM assignment WHERE teacherId = -1";
            return getAssignments(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Assignment> getAssignments(String query) throws SQLException {
        ResultSet res = statement.executeQuery(query);
        ArrayList<Assignment> assignmentList = new ArrayList<>();
        while (res.next()) {
            Assignment assignment = new Assignment(
                    res.getInt("courseId"),
                    res.getInt("teacherId"),
                    res.getString("groupType"),
                    res.getInt("groupNumber"),
                    res.getDouble("numHours"));
            assignment.setId(res.getInt("id"));
            assignmentList.add(assignment);
        }
        return assignmentList;
    }
}
