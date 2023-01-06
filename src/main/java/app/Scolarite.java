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
    QueryService qs;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Scolarite() {};

    @PostConstruct
    public void init() throws SQLException {
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
                          int thresholdTP) throws SQLException, URISyntaxException, IOException {

        qs.addCourse(name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP);
    }

    public void updateCourseById(int id, String name, int semester, double numCMHours, double numTDHours, double numTPHours, int numStudents, int thresholdCM, int thresholdTD, int thresholdTP) {
        qs.updateCourseById(id, name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP);
    }

    public void deleteCourse(int id) {
        qs.deleteCourse(id);
    }

    public List<UE> obtainCourseList() throws SQLException {
        return qs.obtainCourseList();
    }

    public List<UE> searchCourseById(int id) throws NoResultException {
        return qs.searchCourseById(id);
    }

    public List<UE> searchCourseByName(String name) throws NoResultException {
        return qs.searchCourseByName(name);
    }

    public List<UE> searchCourseBySemester(int semester) throws NoResultException {
        return qs.searchCourseBySemester(semester);
    }
}
