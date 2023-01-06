package app;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
    @Inject
    QueryService qs;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public Gdd() {};

    @PostConstruct
    public void init() throws SQLException {
        LOGGER.info("Creation!!");
    }

    public void updateAssignmentById(int id, int teacherId) {
        qs.updateAssignmentById(id, teacherId);
    }

    public void deleteAssignment(int id) {
        qs.deleteAssignment(id);
    }

    public List<Assignment> obtainAssignmentList() throws SQLException {
        return qs.obtainAssignmentList();
    }

    public List<Assignment> searchAssignmentById(int id) throws NoResultException {
        return qs.searchAssignmentById(id);
    }

    public List<Assignment> searchAssignmentByCourseId(int courseId) throws NoResultException {
        return qs.searchAssignmentByCourseId(courseId);
    }

    public List<Assignment> searchAssignmentByTeacherId(int teacherId) throws NoResultException {
        return qs.searchAssignmentByTeacherId(teacherId);
    }

    public List<Assignment> searchUnassigned() throws NoResultException {
        return qs.searchUnassigned();
    }
}
