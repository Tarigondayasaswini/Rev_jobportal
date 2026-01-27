package org.jobportal.dao;

import org.jobportal.config.DBConnection;
import org.jobportal.model.PasswordRecovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordRecoveryDaoImpl implements PasswordRecoveryDao {

    private static final Logger logger =
            LoggerFactory.getLogger(PasswordRecoveryDaoImpl.class);

    @Override
    public boolean save(PasswordRecovery recovery) {

        String sql = """
            INSERT INTO password_recovery
            (user_id, security_question, security_answer)
            VALUES (?, ?, ?)
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, recovery.getUserId());
            ps.setString(2, recovery.getSecurityQuestion());
            ps.setString(3, recovery.getSecurityAnswer());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Saving password recovery failed", e);
            return false;
        }
    }

    @Override
    public PasswordRecovery findByUserId(long userId) {

        String sql =
                "SELECT * FROM password_recovery WHERE user_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PasswordRecovery pr = new PasswordRecovery();
                pr.setUserId(rs.getLong("user_id"));
                pr.setSecurityQuestion(rs.getString("security_question"));
                pr.setSecurityAnswer(rs.getString("security_answer"));
                return pr;
            }

        } catch (SQLException e) {
            logger.error("Fetch password recovery failed", e);
        }
        return null;
    }
}

