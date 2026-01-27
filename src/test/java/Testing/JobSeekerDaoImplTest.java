package Testing;

import org.jobportal.config.DBConnection;
import org.jobportal.dao.JobSeekerDao;
import org.jobportal.dao.JobSeekerDaoImpl;
import org.jobportal.model.JobSeekerProfile;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class JobSeekerDaoImplTest {

    private JobSeekerDao dao;
    private long seekerId;

    @BeforeEach
    void setUp() throws Exception {

        dao = new JobSeekerDaoImpl();

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps =
                     con.prepareStatement(
                             "INSERT INTO users (email, password_hash, role, is_active) VALUES (?, ?, ?, true)",
                             PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "seeker_" + System.currentTimeMillis() + "@mail.com");
            ps.setString(2, "hash");
            ps.setString(3, "JOB_SEEKER");
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            rs.next();
            seekerId = rs.getLong(1);
        }
    }

    @Test
    void testCreateAndFetchProfile() {

        JobSeekerProfile profile = new JobSeekerProfile();
        profile.setSeekerId(seekerId);
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setTotalExperience(2);
        profile.setLocation("Hyderabad");

        assertTrue(dao.createProfile(profile));
        assertNotNull(dao.getProfileBySeekerId(seekerId));
    }

    @AfterEach
    void cleanup() throws Exception {

        try (Connection con = DBConnection.getInstance()) {

            con.prepareStatement(
                    "DELETE FROM job_seeker_profile WHERE seeker_id=" + seekerId
            ).executeUpdate();

            con.prepareStatement(
                    "DELETE FROM users WHERE user_id=" + seekerId
            ).executeUpdate();
        }
    }
}
