
package org.jobportal.dao;

import org.jobportal.config.DBConnection;
import org.jobportal.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserDaoImpl implements UserDao {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDaoImpl.class);

    private static final String INSERT_USER =
            "INSERT INTO users (email, password_hash, role, company_id) VALUES (?, ?, ?, ?)";

    @Override
    public boolean registerUser(User user) {
        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(INSERT_USER)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            if (user.getCompanyId() != null) {
                ps.setLong(4, user.getCompanyId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("User registration failed", e);
            return false;
        }
    }

    @Override
    public User login(String email, String password) {

        String sql = """
            SELECT user_id, email, role, company_id, is_active
            FROM users
            WHERE email=? AND password_hash=? AND is_active=true
            """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getLong("user_id"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setCompanyId((Long) rs.getObject("company_id"));
                user.setActive(rs.getBoolean("is_active"));
                return user;
            }

        } catch (SQLException e) {
            logger.error("Login failed", e);
        }
        return null;
    }

    @Override
    public boolean verifyCurrentPassword(long userId, String currentPassword) {

        String sql = "SELECT 1 FROM users WHERE user_id=? AND password_hash=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, currentPassword);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            logger.error("Password verification failed", e);
            return false;
        }
    }


    @Override
    public User findByEmail(String email) {

        String sql = """
        SELECT user_id, email, role, company_id, is_active
        FROM users
        WHERE email=?
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getLong("user_id"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setCompanyId((Long) rs.getObject("company_id"));
                user.setActive(rs.getBoolean("is_active"));
                return user;
            }

        } catch (SQLException e) {
            logger.error("Find user by email failed", e);
        }
        return null;
    }


    @Override
    public boolean updatePasswordByUserId(long userId, String newPassword) {

        String sql = "UPDATE users SET password_hash=? WHERE user_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setLong(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Password update failed", e);
            return false;
        }
    }



    @Override
    public boolean updateCompanyId(long userId, long companyId) {

        String sql = "UPDATE users SET company_id=? WHERE user_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, companyId);
            ps.setLong(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Failed to update company ID", e);
            return false;
        }
    }


}


