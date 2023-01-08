package app;

import app.Acc.Acc;

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
public class Auth implements Serializable {
    @Inject
    QueryService qs;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Auth() {}

    @PostConstruct
    public void init() {
        LOGGER.info("Login Service Creation");
    }

    public void addAccount(String username, String password, String role) {
        qs.addAccount(username, password, role);
    }

    public void deleteAccount(String username) {
        qs.deleteAccount(username);
    }

    public List<Acc> obtainAccountList() {
        return qs.obtainAccountList();
    }

    public Acc searchAccountByUsername(String username) throws NoResultException {
        return qs.searchAccountByUsername(username);
    }
}
