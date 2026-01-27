
package org.jobportal.service;

import org.jobportal.dao.UserDao;
import org.jobportal.dao.UserDaoImpl;
import org.jobportal.model.User;
import org.jobportal.util.PasswordUtil;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();

    public boolean register(User user) {

        String hashedPassword =
                PasswordUtil.hashPassword(user.getPassword());

        user.setPassword(hashedPassword);

        return userDao.registerUser(user);
    }

    public User login(String email, String password) {

        String hashedPassword =
                PasswordUtil.hashPassword(password);

        return userDao.login(email, hashedPassword);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean updatePasswordByUserId(long userId, String newPassword) {
        return userDao.updatePasswordByUserId(userId, newPassword);
    }

    @Override
    public boolean changePassword(long userId,
                                  String currentPassword,
                                  String newPassword) {

        if (!userDao.verifyCurrentPassword(userId, currentPassword)) {
            return false;
        }
        return userDao.updatePasswordByUserId(userId, newPassword);
    }
    @Override
    public boolean linkCompany(long userId, long companyId) {
        return userDao.updateCompanyId(userId, companyId);
    }


}




