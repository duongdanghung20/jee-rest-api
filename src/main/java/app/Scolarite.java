package app;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;


@Transactional
@RequestScoped
public class Scolarite implements Serializable {
    @Inject
    QueryService qs;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Scolarite() {}

    @PostConstruct
    public void init() {
        LOGGER.info("Creation Scolarite!");
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

        qs.addCourse(name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP);
    }

    public void updateCourseById(int id, String name, int semester, double numCMHours, double numTDHours, double numTPHours, int numStudents, int thresholdCM, int thresholdTD, int thresholdTP) {
        qs.updateCourseById(id, name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP);
    }

    public void deleteCourse(int id) {
        qs.deleteCourse(id);
    }

    public List<UE> obtainCourseList() {
        return qs.obtainCourseList();
    }

    public UE searchCourseById(int id) throws NoResultException {
        return qs.searchCourseById(id);
    }

    public List<UE> searchCourseByName(String name) throws NoResultException {
        return qs.searchCourseByName(name);
    }

    public List<UE> searchCourseBySemester(int semester) throws NoResultException {
        return qs.searchCourseBySemester(semester);
    }
}
