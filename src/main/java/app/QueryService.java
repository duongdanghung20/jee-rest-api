package app;

import app.Assignment.Assignment;
import app.Enseignant.Enseignant;
import app.UE.UE;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;


@Transactional
@RequestScoped
public class QueryService implements Serializable {
    @PersistenceContext(unitName = "rhDB")
    EntityManagerFactory emfRh;

    @PersistenceContext(unitName = "scolariteDB")
    EntityManagerFactory emfScolarite;

    @PersistenceContext(unitName = "gddDB")
    EntityManagerFactory emfGdd;

    @PersistenceContext(unitName = "rhDB")
    EntityManager emRh;

    @PersistenceContext(unitName = "scolariteDB")
    EntityManager emScolarite;

    @PersistenceContext(unitName = "gddDB")
    EntityManager emGdd;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public QueryService() {}

    @PostConstruct
    public void init() {
        emRh = emfRh.createEntityManager();
        emScolarite = emfScolarite.createEntityManager();
        emGdd = emfGdd.createEntityManager();
    }

    @PreDestroy
    public void destroy() {
    }

    public void addTeacher(String firstName,
                           String lastName,
                           float eq,
                           String dept,
                           String service,
                           int numDisHours,
                           int maxOvtHours) {
        Enseignant teacher = new Enseignant(firstName, lastName, eq, dept, service, numDisHours, maxOvtHours);

        emRh.getTransaction().begin();
        emRh.persist(teacher);
        emRh.getTransaction().commit();
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

            emRh.getTransaction().begin();

            Enseignant teacher = emRh.find(Enseignant.class, id);
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacher.setNumHours(numHours);
            teacher.setEq(eq);
            teacher.setDept(dept);
            teacher.setService(service);
            teacher.setNumDisHours(numDisHours);
            teacher.setMaxOvtHours(maxOvtHours);

            emRh.getTransaction().commit();

        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        }
    }

    public void deleteTeacher(int id) {
        try {
            Enseignant teacher = searchTeacherById(id);

            emRh.getTransaction().begin();
            emRh.remove(teacher);
            emRh.getTransaction().commit();


            List<Assignment> thisTeacherAssignments = searchAssignmentByTeacherId(id);
            for (Assignment assignment : thisTeacherAssignments) {
                updateAssignmentById(assignment.getId(),-1);
            }
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        }
    }

    public List<Enseignant> obtainTeacherList() {

        TypedQuery<Enseignant> query = emRh.createQuery("SELECT OBJECT (e) FROM Enseignant e", Enseignant.class);

        return query.getResultList();
    }

    public Enseignant searchTeacherById(int id) throws NoResultException {
        try {

            return emRh.find(Enseignant.class, id);
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        }
    }

    public List<Enseignant> searchTeacherByName(String name) throws NoResultException {
        try {
            
            TypedQuery<Enseignant> query = emRh.createQuery("SELECT OBJECT (e) FROM Enseignant e WHERE LOWER(e.firstName) LIKE CONCAT('%', LOWER(:pattern), '%') OR LOWER(e.lastName) LIKE CONCAT('%', LOWER(:pattern), '%')", Enseignant.class);
            query.setParameter("pattern", name);

            return query.getResultList();
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        }
    }
    public void addCourse(String name,
                          int semester,
                          double numCMHours,
                          double numTDHours,
                          double numTPHours,
                          int numStudents,
                          int thresholdCM,
                          int thresholdTD,
                          int thresholdTP) {

        UE course = new UE(name, semester, numCMHours, numTDHours, numTPHours,
                numStudents, thresholdCM, thresholdTD, thresholdTP);

        
        emScolarite.getTransaction().begin();
        emScolarite.persist(course);
        emScolarite.getTransaction().commit();
        

        // Initialize new assignments when add new course
        UE latestCourseAdded = searchLatestInsertedCourse();

        for (int i = 1; i <= latestCourseAdded.getNumCMGroups(); i++) {
            addAssignment(latestCourseAdded.getId(), -1, "CM", i, latestCourseAdded.getNumCMHours());
        }
        for (int i = 1; i <= latestCourseAdded.getNumTDGroups(); i++) {
            addAssignment(latestCourseAdded.getId(), -1, "TD", i, latestCourseAdded.getNumTDHours());
        }
        for (int i = 1; i <= latestCourseAdded.getNumTPGroups(); i++) {
            addAssignment(latestCourseAdded.getId(), -1, "TP", i, latestCourseAdded.getNumTPHours());
        }

        LOGGER.info("Added new course ####################################!");
    }

    public void updateCourseById(int id, String name, int semester, double numCMHours, double numTDHours, double numTPHours, int numStudents, int thresholdCM, int thresholdTD, int thresholdTP) {
        try {
            
            UE ueUpdate = emScolarite.find(UE.class, id);

            int prevNumCMGroups = ueUpdate.getNumCMGroups();
            int prevNumTDGroups = ueUpdate.getNumTDGroups();
            int prevNumTPGroups = ueUpdate.getNumTPGroups();

            int numCMGroups = (int) Math.ceil((float) numStudents / thresholdCM);
            int numTDGroups = (int) Math.ceil((float) numStudents / thresholdTD);
            int numTPGroups = (int) Math.ceil((float) numStudents / thresholdTP);

            double prevNumCMHours = ueUpdate.getNumCMHours();
            double prevNumTDHours = ueUpdate.getNumTDHours();
            double prevNumTPHours = ueUpdate.getNumTPHours();

            emScolarite.getTransaction().begin();

            ueUpdate.setName(name);
            ueUpdate.setSemester(semester);
            ueUpdate.setNumCMHours(numCMHours);
            ueUpdate.setNumTDHours(numTDHours);
            ueUpdate.setNumTPHours(numTPHours);
            ueUpdate.setNumStudents(numStudents);
            ueUpdate.setThresholdCM(thresholdCM);
            ueUpdate.setThresholdTD(thresholdTD);
            ueUpdate.setThresholdTP(thresholdTP);
            ueUpdate.setNumCMGroups(numCMGroups);
            ueUpdate.setNumTDGroups(numTDGroups);
            ueUpdate.setNumTPGroups(numTPGroups);

            emScolarite.getTransaction().commit();
            

            // Update assignments according to new number of groups
            int differenceCM = numCMGroups - prevNumCMGroups;
            int differenceTD = numTDGroups - prevNumTDGroups;
            int differenceTP = numTPGroups - prevNumTPGroups;

            if (differenceCM > 0) {
                for (int i = prevNumCMGroups + 1; i <= numCMGroups; i++) {
                    addAssignment(id, -1, "CM", i, numCMHours);
                }
            }
            else if (differenceCM < 0) {
                List<Assignment> thisCourseAssignments = searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("CM") && assignment.getGroupNumber() > numCMGroups) {
                        deleteAssignment(assignment.getId());
                    }
                }
            }

            if (differenceTD > 0) {
                for (int i = prevNumTDGroups + 1; i <= numTDGroups; i++) {
                    addAssignment(id, -1, "TD", i, numTDHours);
                }
            }
            else if (differenceTD < 0) {
                List<Assignment> thisCourseAssignments = searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("TD") && assignment.getGroupNumber() > numTDGroups) {
                        deleteAssignment(assignment.getId());
                    }
                }
            }

            if (differenceTP > 0) {
                for (int i = prevNumTPGroups + 1; i <= numTPGroups; i++) {
                    addAssignment(id, -1, "TP", i, numTPHours);
                }
            }
            else if (differenceTP < 0) {
                List<Assignment> thisCourseAssignments = searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("TP") && assignment.getGroupNumber() > numTPGroups) {
                        deleteAssignment(assignment.getId());
                    }
                }
            }

            // Update assignment according to new number of hours
            if (prevNumCMHours != numCMHours) {
                List<Assignment> thisCourseAssignments = searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("CM")) {
                        updateAssignmentNumHoursById(assignment.getId(), numCMHours);
                    }
                }
            }
            if (prevNumTDHours != numTDHours) {
                List<Assignment> thisCourseAssignments = searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("TD")) {
                        updateAssignmentNumHoursById(assignment.getId(), numTDHours);
                    }
                }
            }
            if (prevNumTPHours != numTPHours) {
                List<Assignment> thisCourseAssignments = searchAssignmentByCourseId(id);
                for (Assignment assignment : thisCourseAssignments) {
                    if (assignment.getGroupType().equalsIgnoreCase("TP")) {
                        updateAssignmentNumHoursById(assignment.getId(), numTPHours);
                    }
                }
            }

        } catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }

    public void deleteCourse(int id) {
        try {
            UE course = searchCourseById(id);
            
            emScolarite.getTransaction().begin();
            emScolarite.remove(course);
            emScolarite.getTransaction().commit();

            deleteAssignmentByCourseId(id);

        } catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }

    public List<UE> obtainCourseList() {

        TypedQuery<UE> query = emScolarite.createQuery("SELECT OBJECT (u) FROM UE u", UE.class);

        return query.getResultList();
    }

    public UE searchCourseById(int id) throws NoResultException {
        try {

            return emScolarite.find(UE.class, id);
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        }
    }

    public List<UE> searchCourseByName(String name) throws NoResultException {
        try {

            TypedQuery<UE> query = emScolarite.createQuery("SELECT OBJECT (u) FROM UE u WHERE LOWER(u.name) LIKE CONCAT('%', LOWER(:pattern), '%')", UE.class);
            query.setParameter("pattern", name);

            return query.getResultList();
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }

    public List<UE> searchCourseBySemester(int semester) throws NoResultException {
        try {

            TypedQuery<UE> query = emScolarite.createQuery("SELECT OBJECT (u) FROM UE u WHERE u.semester = :semester", UE.class);
            query.setParameter("semester", semester);

            return query.getResultList();
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }

    public UE searchLatestInsertedCourse() throws NoResultException {
        try {

            TypedQuery<UE> query = emScolarite.createQuery("SELECT OBJECT (u) FROM UE u WHERE u.id = ( SELECT MAX(u.id) FROM UE )", UE.class);

            return query.getSingleResult();
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }


    public void addAssignment(int courseId, int teacherId, String groupType, int groupNumber, double numHours) {

        Assignment a = new Assignment(courseId, teacherId, groupType, groupNumber, numHours);

        
        emGdd.getTransaction().begin();
        emGdd.persist(a);
        emGdd.getTransaction().commit();
        

        LOGGER.info("Added new assignment: Course "  + courseId + " " + groupType + " group " + groupNumber + " for teacher " + teacherId);
    }

    public void updateAssignmentById(int id, int teacherId) {
        try {
            
            emGdd.getTransaction().begin();

            Assignment a = emGdd.find(Assignment.class, id);
            a.setTeacherId(teacherId);

            emGdd.getTransaction().commit();
            
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public void updateAssignmentNumHoursById(int id, double numHours) {
        try {
            
            emGdd.getTransaction().begin();

            Assignment a = emGdd.find(Assignment.class, id);
            a.setNumHours(numHours);

            emGdd.getTransaction().commit();
            
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public void deleteAssignment(int id) {
        try {
            Assignment a = searchAssignmentById(id);
            
            emGdd.getTransaction().begin();
            emGdd.remove(a);
            emGdd.getTransaction().commit();
            
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public void deleteAssignmentByCourseId(int courseId) {
        try {
            List<Assignment> assignmentList = searchAssignmentByCourseId(courseId);
            for (Assignment a : assignmentList) {
                deleteAssignment(a.getId());
            }
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public List<Assignment> obtainAssignmentList() {

        TypedQuery<Assignment> query = emGdd.createQuery("SELECT OBJECT (a) FROM Assignment a", Assignment.class);

        return query.getResultList();
    }

    public Assignment searchAssignmentById(int id) throws NoResultException {
        try {

            return emGdd.find(Assignment.class, id);
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public List<Assignment> searchAssignmentByCourseId(int courseId) throws NoResultException {
        try {

            TypedQuery<Assignment> query = emGdd.createQuery("SELECT OBJECT (a) FROM Assignment a WHERE a.courseId = :courseId", Assignment.class);
            query.setParameter("courseId", courseId);

            return query.getResultList();
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public List<Assignment> searchAssignmentByTeacherId(int teacherId) throws NoResultException {
        try {

            TypedQuery<Assignment> query = emGdd.createQuery("SELECT OBJECT (a) FROM Assignment a WHERE a.teacherId = :teacherId", Assignment.class);
            query.setParameter("teacherId", teacherId);

            return query.getResultList();
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }
}
