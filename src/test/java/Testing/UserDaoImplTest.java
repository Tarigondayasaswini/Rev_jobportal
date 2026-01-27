package Testing;

import org.jobportal.config.DBConnection;
import org.jobportal.dao.UserDao;
import org.jobportal.dao.UserDaoImpl;
import org.jobportal.model.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {

    private UserDao userDao;
    private long userId;
    private String email;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl();
        email = "test_user_" + System.currentTimeMillis() + "@mail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("hashed_test_pass"); // password_hash
        user.setRole("JOB_SEEKER");
        user.setCompanyId(null);

        assertTrue(userDao.registerUser(user));

        User loggedIn = userDao.login(email, "hashed_test_pass");
        assertNotNull(loggedIn);
        userId = loggedIn.getUserId();
    }

    @Test
    void testLoginSuccess() {
        assertNotNull(userDao.login(email, "hashed_test_pass"));
    }

    @AfterEach
    void cleanup() throws Exception {
        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps =
                     con.prepareStatement("DELETE FROM users WHERE user_id=?")) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }
}
