package Testing;

import org.jobportal.config.DBConnection;
import org.jobportal.dao.JobDao;
import org.jobportal.dao.JobDaoImpl;
import org.jobportal.model.Job;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JobDaoImplTest {

    private JobDao jobDao;
    private long companyId;
    private long jobId;

    @BeforeEach
    void setUp() throws Exception {
        jobDao = new JobDaoImpl();

        // 🔹 CREATE COMPANY (PARENT)
        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps =
                     con.prepareStatement(
                             "INSERT INTO company(name, industry, company_size, location) VALUES (?,?,?,?)",
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "DAO Company");
            ps.setString(2, "IT");
            ps.setString(3, "10-50");
            ps.setString(4, "Hyderabad");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            companyId = rs.getLong(1);
        }
    }

    @Test
    void testCreateAndExistsJob() {

        Job job = new Job();
        job.setCompanyId(companyId);
        job.setTitle("DAO Java Dev");
        job.setDescription("Test job");
        job.setSkills("Java");
        job.setExperience(2);
        job.setEducation("B.Tech");
        job.setLocation("Remote");
        job.setSalaryMin(BigDecimal.valueOf(30000));
        job.setSalaryMax(BigDecimal.valueOf(50000));
        job.setJobType("FULL_TIME");
        job.setDeadline(LocalDate.now().plusDays(5));

        jobId = jobDao.createJob(job);
        assertTrue(jobId > 0);
        assertTrue(jobDao.existsById(jobId));
    }

    @AfterEach
    void cleanup() throws Exception {

        try (Connection con = DBConnection.getInstance()) {

            con.prepareStatement(
                    "DELETE FROM job_post WHERE job_id=" + jobId
            ).executeUpdate();

            con.prepareStatement(
                    "DELETE FROM company WHERE company_id=" + companyId
            ).executeUpdate();
        }
    }
}

