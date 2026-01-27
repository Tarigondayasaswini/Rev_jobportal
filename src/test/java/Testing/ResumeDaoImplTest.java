package Testing;

import org.jobportal.config.DBConnection;
import org.jobportal.dao.ResumeDao;
import org.jobportal.dao.ResumeDaoImpl;
import org.jobportal.model.Resume;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class ResumeDaoImplTest {

    private ResumeDao dao;
    private long seekerId;

    @BeforeEach
    void setUp() throws Exception {

        dao = new ResumeDaoImpl();
        Connection con = DBConnection.getInstance();

        /* -------- CREATE USER -------- */
        PreparedStatement psUser =
                con.prepareStatement(
                        "INSERT INTO users (email, password_hash, role, is_active) VALUES (?, ?, ?, true)",
                        PreparedStatement.RETURN_GENERATED_KEYS
                );

        psUser.setString(1, "resume_" + System.currentTimeMillis() + "@mail.com");
        psUser.setString(2, "hash");
        psUser.setString(3, "JOB_SEEKER");
        psUser.executeUpdate();

        ResultSet rs = psUser.getGeneratedKeys();
        rs.next();
        seekerId = rs.getLong(1);

        /* -------- CREATE JOB SEEKER PROFILE (FK REQUIRED) -------- */
        PreparedStatement psProfile =
                con.prepareStatement(
                        "INSERT INTO job_seeker_profile " +
                                "(seeker_id, first_name, last_name, phone, total_experience, location, profile_completed) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)"
                );

        psProfile.setLong(1, seekerId);
        psProfile.setString(2, "John");
        psProfile.setString(3, "Doe");
        psProfile.setString(4, "9999999999");
        psProfile.setInt(5, 2);
        psProfile.setString(6, "Hyderabad");
        psProfile.setBoolean(7, true);
        psProfile.executeUpdate();
    }

    @Test
    void testCreateAndFetchResume() {

        Resume resume = new Resume();
        resume.setSeekerId(seekerId);
        resume.setObjective("Backend Developer");
        resume.setEducation("B.Tech");
        resume.setExperience("2 years");
        resume.setSkills("Java");
        resume.setProjects("JUnit");

        assertTrue(dao.createResume(resume));

        Resume fetched = dao.getResumeBySeekerId(seekerId);

        assertNotNull(fetched);
        assertEquals("Java", fetched.getSkills());
    }

    @AfterEach
    void cleanup() throws Exception {

        Connection con = DBConnection.getInstance();

        con.prepareStatement(
                "DELETE FROM resume WHERE seeker_id=" + seekerId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM job_seeker_profile WHERE seeker_id=" + seekerId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM users WHERE user_id=" + seekerId
        ).executeUpdate();
    }
}
