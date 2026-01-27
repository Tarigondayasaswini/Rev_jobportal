package org.jobportal.service;

import org.jobportal.model.PasswordRecovery;

public interface PasswordRecoveryService {

    boolean saveRecoveryDetails(PasswordRecovery recovery);

    PasswordRecovery getRecoveryDetails(long userId);
}

