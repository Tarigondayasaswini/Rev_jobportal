

package org.jobportal.service;

import org.jobportal.model.User;

public interface UserService {
    boolean register(User user);
    User login(String email, String password);
    User findByEmail(String email);

    boolean updatePasswordByUserId(long userId, String newPassword);
    boolean changePassword(long userId, String currentPassword, String newPassword);
    boolean linkCompany(long userId, long companyId);


}
