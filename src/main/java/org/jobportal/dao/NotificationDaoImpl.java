package org.jobportal.dao;

import org.jobportal.config.DBConnection;
import org.jobportal.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class NotificationDaoImpl implements NotificationDao {

    private static final Logger logger =
            LoggerFactory.getLogger(NotificationDaoImpl.class);

    @Override
    public boolean save(Notification notification) {

        String sql = """
            INSERT INTO notification (user_id, message)
            VALUES (?, ?)
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, notification.getUserId());
            ps.setString(2, notification.getMessage());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Save notification failed", e);
            return false;
        }
    }

    @Override
    public List<Notification> findByUser(long userId) {

        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notification WHERE user_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setNotificationId(rs.getLong("notification_id"));
                n.setUserId(rs.getLong("user_id"));
                n.setMessage(rs.getString("message"));
                n.setRead(rs.getBoolean("is_read"));
                n.setCreatedAt(
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                list.add(n);
            }

        } catch (SQLException e) {
            logger.error("Fetch notifications failed", e);
        }
        return list;
    }

    @Override
    public boolean markAsRead(long notificationId) {

        String sql =
                "UPDATE notification SET is_read=true WHERE notification_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, notificationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Mark notification read failed", e);
            return false;
        }
    }
}

