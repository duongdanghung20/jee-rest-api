package app;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Transactional
@RequestScoped
public class RH implements Serializable {

    @Inject
    Gdd gdd;
    Statement statement;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public RH() {};

    @PostConstruct
    public void init() throws SQLException {
        String urlDB = "jdbc:derby://localhost:1527/appDB;create=true";
        String username = "admin";
        String password = "admin";
        Connection con = DriverManager.getConnection(urlDB, username, password);
        statement = con.createStatement();

        // Create table enseignant if not exist
        try {
            String queryCreateEnseignant =
                    "CREATE TABLE enseignant (" +
                            "id INTEGER GENERATED BY DEFAULT AS IDENTITY, " +
                            "firstName VARCHAR(255) NOT NULL, " +
                            "lastName VARCHAR(255) NOT NULL, " +
                            "numHours DOUBLE NOT NULL, " +
                            "eq FLOAT NOT NULL, " +
                            "dept VARCHAR(255) NOT NULL, " +
                            "service VARCHAR(255) NOT NULL, " +
                            "numDisHours INTEGER NOT NULL, " +
                            "maxOvtHours INTEGER NOT NULL, " +
                            "PRIMARY KEY (id)" +
                    ")";
            statement.execute(queryCreateEnseignant);
        } catch (SQLException e) {
            if (e.getSQLState().equalsIgnoreCase("X0Y32")) {
                LOGGER.info("Ignore create table enseignant because already existed ##");
            }
            else {
                LOGGER.warning(e.getMessage());
            }
        }
        LOGGER.info("Creation");
    }

    public void addTeacher(String firstName,
                           String lastName,
                           float eq,
                           String dept,
                           String service,
                           int numDisHours,
                           int maxOvtHours) throws SQLException {
        Enseignant teacher = new Enseignant(firstName, lastName, eq, dept, service, numDisHours, maxOvtHours);
        String queryAddTeacher = String.format(
                "INSERT INTO enseignant (firstName, lastName, numHours, eq, dept, service, numDisHours, maxOvtHours) " +
                        "VALUES ('%s', '%s', %f, %f, '%s', '%s', %d, %d)",
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getNumHours(),
                teacher.getEq(),
                teacher.getDept(),
                teacher.getService(),
                teacher.getNumDisHours(),
                teacher.getMaxOvtHours());
        statement.execute(queryAddTeacher);
    }

    public void updateTeacherById(int id,
                                  String firstName,
                                  String lastName,
                                  float eq,
                                  String dept,
                                  String service,
                                  int numDisHours,
                                  int maxOvtHours) {
        try {
            double numHours = 0;
            if (service.equalsIgnoreCase("Enseignant-chercheur")) {
                numHours = 192;
            }
            else if (service.equalsIgnoreCase("Enseignant")) {
                numHours = 384;
            }
            else if (service.equalsIgnoreCase("Doctorant")) {
                numHours = 64;
                maxOvtHours = 0;
            }
            String query = String.format("UPDATE enseignant SET firstName = '%s', lastName = '%s', numHours = %f, eq = %f, dept = '%s', service = '%s', numDisHours = %d, maxOvtHours = %d " +
                    "WHERE id = %d", firstName, lastName, numHours, eq, dept, service, numDisHours, maxOvtHours, id);
            statement.execute(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTeacher(int id) {
        try {
            String query = String.format("DELETE FROM enseignant WHERE id = %d", id);
            statement.execute(query);

            List<Assignment> thisTeacherAssignments = this.gdd.searchAssignmentByTeacherId(id);
            for (Assignment assignment : thisTeacherAssignments) {
                this.gdd.updateAssignmentById(assignment.getId(),-1);
            }
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Enseignant> obtainTeacherList() throws SQLException {
        String query = "SELECT * FROM enseignant";
        return getTeachers(query);
    }

    public List<Enseignant> searchTeacherById(int id) throws NoResultException {
        try {
            String query = String.format("SELECT * FROM enseignant WHERE id = %d", id);
            return getTeachers(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Enseignant> searchTeacherByName(String name) throws NoResultException {
        try {
            String query = String.format("SELECT * FROM enseignant WHERE LOWER(firstName) LIKE ('%%' || '%s' || '%%') OR LOWER(lastName) LIKE ('%%' || '%s' || '%%')", name.toLowerCase(), name.toLowerCase());
            return getTeachers(query);
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Enseignant> getTeachers(String query) throws SQLException {
        ResultSet res = statement.executeQuery(query);
        ArrayList<Enseignant> teacherList = new ArrayList<Enseignant>();
        while (res.next()) {
            Enseignant teacher = new Enseignant(
                    res.getString("firstName"),
                    res.getString("lastName"),
                    res.getFloat("eq"),
                    res.getString("dept"),
                    res.getString("service"),
                    res.getInt("numDisHours"),
                    res.getInt("maxOvtHours"));
            teacher.setId(res.getInt("id"));
            teacherList.add(teacher);
        }
        return teacherList;
    }
}
