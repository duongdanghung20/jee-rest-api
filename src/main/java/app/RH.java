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
public class RH implements Serializable {
    @Inject
    QueryService qs;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public RH() {}

    @PostConstruct
    public void init() {
        LOGGER.info("RH Service Creation");
    }

    public void addTeacher(String firstName,
                           String lastName,
                           float eq,
                           String dept,
                           String service,
                           int numDisHours,
                           int maxOvtHours) {
        qs.addTeacher(firstName, lastName, eq, dept, service, numDisHours, maxOvtHours);
    }

    public void updateTeacherById(int id,
                                  String firstName,
                                  String lastName,
                                  float eq,
                                  String dept,
                                  String service,
                                  int numDisHours,
                                  int maxOvtHours) {
        qs.updateTeacherById(id, firstName, lastName, eq, dept, service, numDisHours, maxOvtHours);
    }

    public void deleteTeacher(int id) {
        qs.deleteTeacher(id);
    }

    public List<Enseignant> obtainTeacherList() {
        return qs.obtainTeacherList();
    }

    public Enseignant searchTeacherById(int id) throws NoResultException {
        return qs.searchTeacherById(id);
    }

    public List<Enseignant> searchTeacherByName(String name) throws NoResultException {
        return qs.searchTeacherByName(name);
    }
}
