package org.jobportal.service;

import org.jobportal.dao.PasswordRecoveryDao;
import org.jobportal.dao.PasswordRecoveryDaoImpl;
import org.jobportal.model.PasswordRecovery;

public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    private final PasswordRecoveryDao dao =
            new PasswordRecoveryDaoImpl();

    @Override
    public boolean saveRecoveryDetails(PasswordRecovery recovery) {
        return dao.save(recovery);
    }

    @Override
    public PasswordRecovery getRecoveryDetails(long userId) {
        return dao.findByUserId(userId);
    }
}
