

package org.jobportal.dao;

import org.jobportal.model.User;

public interface UserDao {

    boolean registerUser(User user);

    User login(String email, String password);

    boolean verifyCurrentPassword(long userId, String currentPassword);
    User findByEmail(String email);

    boolean updatePasswordByUserId(long userId, String newPassword);
    boolean updateCompanyId(long userId, long companyId);


}

