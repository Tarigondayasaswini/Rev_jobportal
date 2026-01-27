
package Testing;

import org.jobportal.config.DBConnection;
import org.jobportal.dao.NotificationDao;
import org.jobportal.dao.NotificationDaoImpl;
import org.jobportal.model.Notification;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDaoImplTest {

    private NotificationDao dao;
    private long userId;

    @BeforeEach
    void setUp() throws Exception {

        dao = new NotificationDaoImpl();
        Connection con = DBConnection.getInstance();

        PreparedStatement ps =
                con.prepareStatement(
                        "INSERT INTO users (email, password_hash, role, is_active) VALUES (?, ?, ?, true)",
                        PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, "notify_" + System.currentTimeMillis() + "@mail.com");
        ps.setString(2, "hash");
        ps.setString(3, "JOB_SEEKER");
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        userId = rs.getLong(1);
    }

    @Test
    void testSaveAndFetchNotification() {

        Notification n = new Notification();
        n.setUserId(userId);
        n.setMessage("Test message");

        assertTrue(dao.save(n));

        List<Notification> list =
                dao.findByUser(userId);

        assertFalse(list.isEmpty());
    }

    @AfterEach
    void cleanup() throws Exception {

        Connection con = DBConnection.getInstance();

        con.prepareStatement(
                "DELETE FROM notification WHERE user_id=" + userId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM users WHERE user_id=" + userId
        ).executeUpdate();
    }
}
