package org.jobportal.dao;

import org.jobportal.model.PasswordRecovery;

public interface PasswordRecoveryDao {

    boolean save(PasswordRecovery recovery);

    PasswordRecovery findByUserId(long userId);
}

