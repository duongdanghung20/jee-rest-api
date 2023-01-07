package app;

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
    @PersistenceContext
    EntityManagerFactory emf;
    @PersistenceContext
    EntityManager em;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public QueryService() {}

    @PostConstruct
    public void init() {
        em = emf.createEntityManager();
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

        em.getTransaction().begin();
        em.persist(teacher);
        em.getTransaction().commit();
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

            em.getTransaction().begin();

            Enseignant teacher = em.find(Enseignant.class, id);
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacher.setNumHours(numHours);
            teacher.setEq(eq);
            teacher.setDept(dept);
            teacher.setService(service);
            teacher.setNumDisHours(numDisHours);
            teacher.setMaxOvtHours(maxOvtHours);

            em.getTransaction().commit();

        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        }
    }

    public void deleteTeacher(int id) {
        try {
            Enseignant teacher = searchTeacherById(id);

            em.getTransaction().begin();
            em.remove(teacher);
            em.getTransaction().commit();


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
        
        TypedQuery<Enseignant> query = em.createQuery("SELECT OBJECT (e) FROM Enseignant e", Enseignant.class);
        List<Enseignant> teacherList = query.getResultList();
        
        return teacherList;
    }

    public Enseignant searchTeacherById(int id) throws NoResultException {
        try {
            
            Enseignant teacher = em.find(Enseignant.class, id);
            
            return teacher;
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        }
    }

    public List<Enseignant> searchTeacherByName(String name) throws NoResultException {
        try {
            
            TypedQuery<Enseignant> query = em.createQuery("SELECT OBJECT (e) FROM Enseignant e WHERE LOWER(e.firstName) LIKE CONCAT('%', LOWER(:pattern), '%') OR LOWER(e.lastName) LIKE CONCAT('%', LOWER(:pattern), '%')", Enseignant.class);
            query.setParameter("pattern", name);
            List<Enseignant> teacherList = query.getResultList();
            
            return teacherList;
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

        
        em.getTransaction().begin();
        em.persist(course);
        em.getTransaction().commit();
        

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
            
            UE ueUpdate = em.find(UE.class, id);

            int prevNumCMGroups = ueUpdate.getNumCMGroups();
            int prevNumTDGroups = ueUpdate.getNumTDGroups();
            int prevNumTPGroups = ueUpdate.getNumTPGroups();

            int numCMGroups = (int) Math.ceil((float) numStudents / thresholdCM);
            int numTDGroups = (int) Math.ceil((float) numStudents / thresholdTD);
            int numTPGroups = (int) Math.ceil((float) numStudents / thresholdTP);

            double prevNumCMHours = ueUpdate.getNumCMHours();
            double prevNumTDHours = ueUpdate.getNumTDHours();
            double prevNumTPHours = ueUpdate.getNumTPHours();

            em.getTransaction().begin();

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

            em.getTransaction().commit();
            

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
            
            em.getTransaction().begin();
            em.remove(course);
            em.getTransaction().commit();
            

            // TODO: delete assignment when delete course using entity manager
            deleteAssignmentByCourseId(id);

        } catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }

    public List<UE> obtainCourseList() {
        
        TypedQuery<UE> query = em.createQuery("SELECT OBJECT (u) FROM UE u", UE.class);
        List<UE> courseList = query.getResultList();
        
        return courseList;
    }

    public UE searchCourseById(int id) throws NoResultException {
        try {
            
            UE course = em.find(UE.class, id);
            
            return course;
        }
        catch (NoResultException e) {
            throw new NoResultException("Teacher not found!");
        }
    }

    public List<UE> searchCourseByName(String name) throws NoResultException {
        try {
            
            TypedQuery<UE> query = em.createQuery("SELECT OBJECT (u) FROM UE u WHERE LOWER(u.name) LIKE CONCAT('%', LOWER(:pattern), '%')", UE.class);
            query.setParameter("pattern", name);
            List<UE> courseList = query.getResultList();
            
            return courseList;
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }

    public List<UE> searchCourseBySemester(int semester) throws NoResultException {
        try {
            
            TypedQuery<UE> query = em.createQuery("SELECT OBJECT (u) FROM UE u WHERE u.semester = :semester", UE.class);
            query.setParameter("semester", semester);
            List<UE> courseList = query.getResultList();
            
            return courseList;
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }

    public UE searchLatestInsertedCourse() throws NoResultException {
        try {
            
            TypedQuery<UE> query = em.createQuery("SELECT OBJECT (u) FROM UE u WHERE u.id = ( SELECT MAX(u.id) FROM UE )", UE.class);
            UE latestCourse = query.getSingleResult();
            
            return latestCourse;
        }
        catch (NoResultException e) {
            throw new NoResultException("Course not found!");
        }
    }


    public void addAssignment(int courseId, int teacherId, String groupType, int groupNumber, double numHours) {

        Assignment a = new Assignment(courseId, teacherId, groupType, groupNumber, numHours);

        
        em.getTransaction().begin();
        em.persist(a);
        em.getTransaction().commit();
        

        LOGGER.info("Added new assignment: Course "  + courseId + " " + groupType + " group " + groupNumber + " for teacher " + teacherId);
    }

    public void updateAssignmentById(int id, int teacherId) {
        try {
            
            em.getTransaction().begin();

            Assignment a = em.find(Assignment.class, id);
            a.setTeacherId(teacherId);

            em.getTransaction().commit();
            
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public void updateAssignmentNumHoursById(int id, double numHours) {
        try {
            
            em.getTransaction().begin();

            Assignment a = em.find(Assignment.class, id);
            a.setNumHours(numHours);

            em.getTransaction().commit();
            
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public void deleteAssignment(int id) {
        try {
            Assignment a = searchAssignmentById(id);
            
            em.getTransaction().begin();
            em.remove(a);
            em.getTransaction().commit();
            
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
        
        TypedQuery<Assignment> query = em.createQuery("SELECT OBJECT (a) FROM Assignment a", Assignment.class);
        List<Assignment> assignmentList = query.getResultList();
        
        return assignmentList;
    }

    public Assignment searchAssignmentById(int id) throws NoResultException {
        try {
            
            Assignment a = em.find(Assignment.class, id);
            
            return a;
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public List<Assignment> searchAssignmentByCourseId(int courseId) throws NoResultException {
        try {
            
            TypedQuery<Assignment> query = em.createQuery("SELECT OBJECT (a) FROM Assignment a WHERE a.courseId = :courseId", Assignment.class);
            query.setParameter("courseId", courseId);
            List<Assignment> assignmentList = query.getResultList();
            
            return assignmentList;
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }

    public List<Assignment> searchAssignmentByTeacherId(int teacherId) throws NoResultException {
        try {
            
            TypedQuery<Assignment> query = em.createQuery("SELECT OBJECT (a) FROM Assignment a WHERE a.teacherId = :teacherId", Assignment.class);
            query.setParameter("teacherId", teacherId);
            List<Assignment> assignmentList = query.getResultList();
            
            return assignmentList;
        }
        catch (NoResultException e) {
            throw new NoResultException("Assignment not found!");
        }
    }
}
