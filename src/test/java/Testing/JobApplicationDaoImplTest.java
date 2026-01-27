package Testing;

import org.jobportal.config.DBConnection;
import org.jobportal.dao.JobApplicationDao;
import org.jobportal.dao.JobApplicationDaoImpl;
import org.jobportal.model.JobApplication;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JobApplicationDaoImplTest {

    private JobApplicationDao dao;

    private long seekerId;
    private long companyId;
    private long jobId;
    private long resumeId;

    @BeforeEach
    void setUp() throws Exception {

        dao = new JobApplicationDaoImpl();
        Connection con = DBConnection.getInstance();

        /* ---------- USER ---------- */
        PreparedStatement psUser =
                con.prepareStatement(
                        "INSERT INTO users (email, password_hash, role, is_active) VALUES (?, ?, ?, true)",
                        PreparedStatement.RETURN_GENERATED_KEYS);

        psUser.setString(1, "seek_" + System.currentTimeMillis() + "@mail.com");
        psUser.setString(2, "hash");
        psUser.setString(3, "JOB_SEEKER");
        psUser.executeUpdate();

        ResultSet rsUser = psUser.getGeneratedKeys();
        rsUser.next();
        seekerId = rsUser.getLong(1);

        /* ---------- JOB SEEKER PROFILE ---------- */
        PreparedStatement psProfile =
                con.prepareStatement(
                        """
                        INSERT INTO job_seeker_profile
                        (seeker_id, first_name, last_name, phone, total_experience, location, profile_completed)
                        VALUES (?, ?, ?, ?, ?, ?, true)
                        """);

        psProfile.setLong(1, seekerId);
        psProfile.setString(2, "John");
        psProfile.setString(3, "Doe");
        psProfile.setString(4, "9999999999");
        psProfile.setInt(5, 2);
        psProfile.setString(6, "HYD");
        psProfile.executeUpdate();

        /* ---------- COMPANY ---------- */
        PreparedStatement psCompany =
                con.prepareStatement(
                        "INSERT INTO company (name, industry, company_size, location) VALUES ('TestCo','IT','10','HYD')",
                        PreparedStatement.RETURN_GENERATED_KEYS);

        psCompany.executeUpdate();
        ResultSet rsCompany = psCompany.getGeneratedKeys();
        rsCompany.next();
        companyId = rsCompany.getLong(1);

        /* ---------- JOB ---------- */
        PreparedStatement psJob =
                con.prepareStatement(
                        """
                        INSERT INTO job_post
                        (company_id, title, description, skills, experience, education,
                         location, job_type, status, deadline)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'OPEN', ?)
                        """,
                        PreparedStatement.RETURN_GENERATED_KEYS);

        psJob.setLong(1, companyId);
        psJob.setString(2, "Java Dev");
        psJob.setString(3, "Backend role");
        psJob.setString(4, "Java");
        psJob.setInt(5, 2);
        psJob.setString(6, "B.Tech");
        psJob.setString(7, "HYD");
        psJob.setString(8, "FULL_TIME");
        psJob.setDate(9, java.sql.Date.valueOf(LocalDate.now().plusDays(5)));
        psJob.executeUpdate();

        ResultSet rsJob = psJob.getGeneratedKeys();
        rsJob.next();
        jobId = rsJob.getLong(1);

        /* ---------- RESUME ---------- */
        PreparedStatement psResume =
                con.prepareStatement(
                        """
                        INSERT INTO resume
                        (seeker_id, objective, education, experience, skills, projects)
                        VALUES (?, ?, ?, ?, ?, ?)
                        """,
                        PreparedStatement.RETURN_GENERATED_KEYS);

        psResume.setLong(1, seekerId);
        psResume.setString(2, "Java Dev");
        psResume.setString(3, "B.Tech");
        psResume.setString(4, "2 years");
        psResume.setString(5, "Java");
        psResume.setString(6, "JUnit");
        psResume.executeUpdate();

        ResultSet rsResume = psResume.getGeneratedKeys();
        rsResume.next();
        resumeId = rsResume.getLong(1);
    }

    @Test
    void testSaveAndFetch() {

        JobApplication app = new JobApplication();
        app.setJobId(jobId);
        app.setSeekerId(seekerId);
        app.setResumeId(resumeId);
        app.setCoverLetter("JUnit Test");

        assertTrue(dao.save(app));

        List<JobApplication> list =
                dao.findBySeeker(seekerId);

        assertFalse(list.isEmpty());
    }

    @AfterEach
    void cleanup() throws Exception {

        Connection con = DBConnection.getInstance();

        con.prepareStatement(
                "DELETE FROM job_application WHERE seeker_id=" + seekerId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM resume WHERE seeker_id=" + seekerId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM job_post WHERE job_id=" + jobId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM job_seeker_profile WHERE seeker_id=" + seekerId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM company WHERE company_id=" + companyId
        ).executeUpdate();

        con.prepareStatement(
                "DELETE FROM users WHERE user_id=" + seekerId
        ).executeUpdate();
    }
}
