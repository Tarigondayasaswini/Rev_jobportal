package org.jobportal.dao;

import org.jobportal.config.DBConnection;
import org.jobportal.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {

    private static final Logger logger =
            LoggerFactory.getLogger(JobDaoImpl.class);



    @Override
    public long createJob(Job job) {

        String sql = """
            INSERT INTO job_post
            (company_id, title, description, skills, experience, education,
             location, salary_min, salary_max, job_type, deadline)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, job.getCompanyId());
            ps.setString(2, job.getTitle());
            ps.setString(3, job.getDescription());
            ps.setString(4, job.getSkills());
            ps.setInt(5, job.getExperience());
            ps.setString(6, job.getEducation());
            ps.setString(7, job.getLocation());
            ps.setBigDecimal(8, job.getSalaryMin());
            ps.setBigDecimal(9, job.getSalaryMax());
            ps.setString(10, job.getJobType());
            ps.setDate(11, Date.valueOf(job.getDeadline()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);

        } catch (SQLException e) {
            logger.error("Job creation failed", e);
        }
        return -1;
    }

    @Override
    public List<Job> getJobsByCompany(long companyId) {

        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM job_post WHERE company_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, companyId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                jobs.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logger.error("Fetch jobs failed", e);
        }
        return jobs;
    }

    @Override
    public List<Job> getOpenJobs() {

        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM job_post WHERE status='OPEN'";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                jobs.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logger.error("Fetch open jobs failed", e);
        }
        return jobs;
    }

    @Override
    public boolean closeJob(long jobId) {

        String sql = "UPDATE job_post SET status='CLOSED' WHERE job_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Close job failed", e);
            return false;
        }
    }



    @Override
    public List<Job> searchJobsWithFilters(
            String title,
            String location,
            Integer experience,
            String companyName,
            Double minSalary,
            Double maxSalary,
            String jobType) {

        List<Job> jobs = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT jp.*, c.name AS company_name
        FROM job_post jp
        JOIN company c ON jp.company_id = c.company_id
        WHERE jp.status = 'OPEN'
    """);

        if (title != null) sql.append(" AND jp.title LIKE ?");
        if (location != null) sql.append(" AND jp.location LIKE ?");
        if (experience != null) sql.append(" AND jp.experience <= ?");
        if (companyName != null) sql.append(" AND c.name LIKE ?");
        if (minSalary != null) sql.append(" AND jp.salary_min >= ?");
        if (maxSalary != null) sql.append(" AND jp.salary_max <= ?");
        if (jobType != null) sql.append(" AND jp.job_type = ?");

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;

            if (title != null) ps.setString(idx++, "%" + title + "%");
            if (location != null) ps.setString(idx++, "%" + location + "%");
            if (experience != null) ps.setInt(idx++, experience);
            if (companyName != null) ps.setString(idx++, "%" + companyName + "%");
            if (minSalary != null) ps.setDouble(idx++, minSalary);
            if (maxSalary != null) ps.setDouble(idx++, maxSalary);
            if (jobType != null) ps.setString(idx++, jobType);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Job job = new Job();
                job.setJobId(rs.getLong("job_id"));
                job.setTitle(rs.getString("title"));
                job.setLocation(rs.getString("location"));
                job.setExperience(rs.getInt("experience"));
                job.setJobType(rs.getString("job_type"));
                job.setSalaryMin(rs.getBigDecimal("salary_min"));
                job.setSalaryMax(rs.getBigDecimal("salary_max"));
                jobs.add(job);
            }

        } catch (SQLException e) {
            logger.error("Job search failed", e);
        }

        return jobs;
    }


    @Override
    public boolean updateJob(Job job) {

        String sql = """
        UPDATE job_post
        SET title=?, description=?, skills=?, experience=?, education=?,
            location=?, salary_min=?, salary_max=?, job_type=?, deadline=?
        WHERE job_id=? AND status <> 'CLOSED'
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, job.getTitle());
            ps.setString(2, job.getDescription());
            ps.setString(3, job.getSkills());
            ps.setInt(4, job.getExperience());
            ps.setString(5, job.getEducation());
            ps.setString(6, job.getLocation());
            ps.setBigDecimal(7, job.getSalaryMin());
            ps.setBigDecimal(8, job.getSalaryMax());
            ps.setString(9, job.getJobType());
            ps.setDate(10, Date.valueOf(job.getDeadline()));
            ps.setLong(11, job.getJobId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Update job failed", e);
            return false;
        }
    }

    @Override
    public boolean reopenJob(long jobId) {

        String sql = "UPDATE job_post SET status='OPEN' WHERE job_id=? AND status='CLOSED'";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Reopen job failed", e);
            return false;
        }
    }

    @Override
    public boolean deleteJob(long jobId) {

        String sql = """
        DELETE FROM job_post
        WHERE job_id=?
        AND job_id NOT IN (
            SELECT job_id FROM job_application
        )
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Delete job failed", e);
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
    public boolean existsById(long jobId) {

        String sql = "SELECT 1 FROM job_post WHERE job_id=? ";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            logger.error("Job existence check failed", e);
            return false;
        }
    }


    @Override
    public boolean isJobOpen(long jobId) {

        String sql =
                "SELECT 1 FROM job_post WHERE job_id=? AND status='OPEN'";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            logger.error("Job open check failed", e);
            return false;
        }
    }



    private Job mapRow(ResultSet rs) throws SQLException {

        Job job = new Job();
        job.setJobId(rs.getLong("job_id"));
        job.setCompanyId(rs.getLong("company_id"));
        job.setTitle(rs.getString("title"));
        job.setDescription(rs.getString("description"));
        job.setSkills(rs.getString("skills"));
        job.setExperience(rs.getInt("experience"));
        job.setEducation(rs.getString("education"));
        job.setLocation(rs.getString("location"));
        job.setSalaryMin(rs.getBigDecimal("salary_min"));
        job.setSalaryMax(rs.getBigDecimal("salary_max"));
        job.setJobType(rs.getString("job_type"));
        job.setStatus(rs.getString("status"));
        job.setDeadline(rs.getDate("deadline").toLocalDate());
        return job;
    }
}

