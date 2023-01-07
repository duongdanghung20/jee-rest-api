package app;


import app.Assignment.Assignment;

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
public class Gdd implements Serializable {
    @Inject
    QueryService qs;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public Gdd() {}

    @PostConstruct
    public void init() {
        LOGGER.info("Creation!!");
    }

    public void updateAssignmentById(int id, int teacherId) {
        qs.updateAssignmentById(id, teacherId);
    }

    public void deleteAssignment(int id) {
        qs.deleteAssignment(id);
    }

    public List<Assignment> obtainAssignmentList() {
        return qs.obtainAssignmentList();
    }

    public Assignment searchAssignmentById(int id) throws NoResultException {
        return qs.searchAssignmentById(id);
    }

    public List<Assignment> searchAssignmentByCourseId(int courseId) throws NoResultException {
        return qs.searchAssignmentByCourseId(courseId);
    }

    public List<Assignment> searchAssignmentByTeacherId(int teacherId) throws NoResultException {
        return qs.searchAssignmentByTeacherId(teacherId);
    }

    public List<Assignment> searchUnassigned() throws NoResultException {
        return searchAssignmentByTeacherId(-1);
    }
}
