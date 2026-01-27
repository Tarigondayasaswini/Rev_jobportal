package org.jobportal.dao;

import org.jobportal.config.DBConnection;
import org.jobportal.model.JobApplication;
import org.jobportal.service.NotificationService;
import org.jobportal.service.NotificationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class JobApplicationDaoImpl implements JobApplicationDao {

    private static final Logger logger =
            LoggerFactory.getLogger(JobApplicationDaoImpl.class);

    private final NotificationService notificationService =
            new NotificationServiceImpl();

    @Override
    public boolean save(JobApplication application) {

        String sql = """
            INSERT INTO job_application
            (job_id, seeker_id, resume_id, cover_letter)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, application.getJobId());
            ps.setLong(2, application.getSeekerId());
            ps.setLong(3, application.getResumeId());
            ps.setString(4, application.getCoverLetter());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<JobApplication> findBySeeker(long seekerId) {

        List<JobApplication> list = new ArrayList<>();

        String sql = "SELECT * FROM job_application WHERE seeker_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, seekerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JobApplication ja = new JobApplication();
                ja.setApplicationId(rs.getLong("application_id"));
                ja.setJobId(rs.getLong("job_id"));
                ja.setSeekerId(rs.getLong("seeker_id"));
                ja.setResumeId(rs.getLong("resume_id"));
                ja.setCoverLetter(rs.getString("cover_letter"));
                ja.setStatus(rs.getString("status"));
                ja.setAppliedAt(rs.getTimestamp("applied_at").toLocalDateTime());
                list.add(ja);
            }

        } catch (SQLException e) {
            logger.error("Fetching applications failed", e);
        }
        return list;
    }


    @Override
    public Long findLatestResumeId(long seekerId) {

        String sql = """
        SELECT resume_id
        FROM resume
        WHERE seeker_id=?
        ORDER BY updated_at DESC
        LIMIT 1
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("resume_id");
            }

        } catch (SQLException e) {
            logger.error("Fetch latest resume failed", e);
        }
        return null;
    }
    @Override
    public List<Object[]> findApplicationsWithJobDetails(long seekerId) {

        List<Object[]> list = new ArrayList<>();

        String sql = """
        SELECT 
            a.application_id,
            j.title,
            c.name AS company_name,
            j.location,
            a.status,
            a.applied_at
        FROM job_application a
        JOIN job_post j ON a.job_id = j.job_id
        JOIN company c ON j.company_id = c.company_id
        WHERE a.seeker_id = ?
        ORDER BY a.applied_at DESC
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, seekerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getLong("application_id"), // ⭐ NEW
                        rs.getString("title"),
                        rs.getString("company_name"),
                        rs.getString("location"),
                        rs.getString("status"),
                        rs.getTimestamp("applied_at").toLocalDateTime()
                });
            }

        } catch (SQLException e) {
            logger.error("Fetch applications failed", e);
        }

        return list;
    }


    @Override
    public boolean withdraw(long applicationId, long seekerId) {

        String sql = """
        UPDATE job_application
        SET status='WITHDRAWN'
        WHERE application_id=? AND seeker_id=?
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, applicationId);
            ps.setLong(2, seekerId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Withdraw application failed", e);
            return false;
        }
    }


    @Override
    public long findSeekerIdByApplication(long applicationId) {

        String sql =
                "SELECT seeker_id FROM job_application WHERE application_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, applicationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("seeker_id");
            }

        } catch (SQLException e) {
            logger.error("Fetch seekerId failed", e);
        }
        return -1;
    }

    @Override
    public List<Object[]> findApplicantsByJob(long jobId) {

        String sql = """
        SELECT
            ja.application_id,
            ja.applied_at,
            ja.status,
            jsp.first_name,
            jsp.last_name,
            jsp.total_experience,
            r.education,
            r.skills,
            r.experience
        FROM job_application ja
        JOIN job_seeker_profile jsp ON ja.seeker_id = jsp.seeker_id
        JOIN resume r ON ja.resume_id = r.resume_id
        WHERE ja.job_id=?
    """;

        List<Object[]> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getLong("application_id"),
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getInt("total_experience"),
                        rs.getString("education"),
                        rs.getString("skills"),
                        rs.getTimestamp("applied_at"),
                        rs.getString("status")
                });
            }

        } catch (SQLException e) {
            logger.error("Fetch applicants failed", e);
        }
        return list;
    }


    @Override
    public List<Object[]> filterApplicants(
            long jobId,
            Integer minExperience,
            String skills,
            String education,
            Date appliedAfter) {

        String sql = """
        SELECT
            ja.application_id,
            ja.applied_at,
            ja.status,
            jsp.first_name,
            jsp.last_name,
            jsp.total_experience,
            r.education,
            r.skills
        FROM job_application ja
        JOIN job_seeker_profile jsp ON ja.seeker_id = jsp.seeker_id
        JOIN resume r ON ja.resume_id = r.resume_id
        WHERE ja.job_id = ?
          AND (? IS NULL OR jsp.total_experience >= ?)
          AND (? IS NULL OR r.skills LIKE CONCAT('%', ?, '%'))
          AND (? IS NULL OR r.education LIKE CONCAT('%', ?, '%'))
          AND (? IS NULL OR ja.applied_at >= ?)
    """;

        List<Object[]> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);

            ps.setObject(2, minExperience);
            ps.setObject(3, minExperience);

            ps.setString(4, skills);
            ps.setString(5, skills);

            ps.setString(6, education);
            ps.setString(7, education);

            ps.setDate(8, appliedAfter);
            ps.setDate(9, appliedAfter);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getLong("application_id"),
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getInt("total_experience"),
                        rs.getString("education"),
                        rs.getString("skills"),
                        rs.getTimestamp("applied_at"),
                        rs.getString("status")
                });
            }

        } catch (SQLException e) {
            logger.error("Filter applicants failed", e);
        }

        return list;
    }


    @Override
    public boolean bulkUpdateStatus(
            List<Long> applicationIds,
            String status,
            String comment) {

        String sql = """
        UPDATE job_application
        SET status = ?
        WHERE application_id = ?
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (Long appId : applicationIds) {
                ps.setString(1, status);
                ps.setLong(2, appId);
                ps.addBatch();
            }

            ps.executeBatch();
            return true;

        } catch (SQLException e) {
            logger.error("Bulk status update failed", e);
            return false;
        }
    }

    @Override
    public Object[] getJobStatistics(long jobId) {

        String sql = """
        SELECT
            COUNT(*) AS total,
            SUM(status='SHORTLISTED') AS shortlisted,
            SUM(status='REJECTED') AS rejected
        FROM job_application
        WHERE job_id=?
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Object[]{
                        rs.getInt("total"),
                        rs.getInt("shortlisted"),
                        rs.getInt("rejected")
                };
            }

        } catch (SQLException e) {
            logger.error("Job statistics fetch failed", e);
        }
        return null;
    }


    @Override
    public List<Long> findMatchingSeekers(
            String skills,
            String location) {

        List<Long> seekerIds = new ArrayList<>();

        String sql = """
        SELECT DISTINCT jsp.seeker_id
        FROM job_seeker_profile jsp
        JOIN resume r ON jsp.seeker_id = r.seeker_id
        WHERE r.skills LIKE ?
        AND (? IS NULL OR jsp.location = ?)
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + skills + "%");
            ps.setString(2, location);
            ps.setString(3, location);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                seekerIds.add(rs.getLong("seeker_id"));
            }

        } catch (SQLException e) {
            logger.error("Finding matching seekers failed", e);
        }
        return seekerIds;
    }


    @Override
    public boolean hasAppliedForJob(long jobId, long seekerId) {

        String sql = """
        SELECT 1
        FROM job_application
        WHERE job_id = ?
          AND seeker_id = ?
          AND status IN ('APPLIED', 'SHORTLISTED')
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            ps.setLong(2, seekerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
//          logger.error("Duplicate application check failed");
        }
        return false;
    }

}

