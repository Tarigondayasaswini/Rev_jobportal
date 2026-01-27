package Testing;

import org.jobportal.config.DBConnection;
import org.jobportal.dao.PasswordRecoveryDao;
import org.jobportal.dao.PasswordRecoveryDaoImpl;
import org.jobportal.model.PasswordRecovery;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class PasswordRecoveryDaoImplTest {

    private PasswordRecoveryDao dao;
    private long userId;

    @BeforeEach
    void setUp() throws Exception {

        dao = new PasswordRecoveryDaoImpl();
        Connection con = DBConnection.getInstance();

        PreparedStatement ps =
                con.prepareStatement(
                        "INSERT INTO users (email, password_hash, role, is_active) VALUES (?, ?, ?, true)",
                        PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, "recover_" + System.currentTimeMillis() + "@mail.com");
        ps.setString(2, "hash");
        ps.setString(3, "JOB_SEEKER");
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        userId = rs.getLong(1);
    }

    @Test
    void testSaveAndFetchRecovery() {

        PasswordRecovery pr = new PasswordRecovery();
        pr.setUserId(userId);
        pr.setSecurityQuestion("Pet?");
        pr.setSecurityAnswer("Dog");

        assertTrue(dao.save(pr));

        PasswordRecovery fetched =
                dao.findByUserId(userId);

        assertNotNull(fetched);
        assertEquals("Dog", fetched.getSecurityAnswer());
    }

    @AfterEach
    void cleanup() throws Exception {

        Connection con = DBConnection.getInstance();

        con.prepareStatement(
                "DELETE FROM password_recovery WHERE user_id=" + userId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM users WHERE user_id=" + userId
        ).executeUpdate();
    }
}
