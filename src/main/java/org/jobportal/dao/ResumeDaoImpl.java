package org.jobportal.dao;

import org.jobportal.config.DBConnection;
import org.jobportal.model.Resume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class ResumeDaoImpl implements ResumeDao {

    private static final Logger logger =
            LoggerFactory.getLogger(ResumeDaoImpl.class);

    @Override
    public boolean createResume(Resume resume) {

        String sql = """
            INSERT INTO resume
            (seeker_id, objective, education, experience, skills, projects)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, resume.getSeekerId());
            ps.setString(2, resume.getObjective());
            ps.setString(3, resume.getEducation());
            ps.setString(4, resume.getExperience());
            ps.setString(5, resume.getSkills());
            ps.setString(6, resume.getProjects());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Create resume failed", e);
            return false;
        }
    }

    @Override
    public Resume getResumeBySeekerId(long seekerId) {

        String sql = "SELECT * FROM resume WHERE seeker_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Resume resume = new Resume();
                resume.setResumeId(rs.getLong("resume_id"));
                resume.setSeekerId(seekerId);
                resume.setObjective(rs.getString("objective"));
                resume.setEducation(rs.getString("education"));
                resume.setExperience(rs.getString("experience"));
                resume.setSkills(rs.getString("skills"));
                resume.setProjects(rs.getString("projects"));
                return resume;
            }

        } catch (SQLException e) {
            logger.error("Fetch resume failed", e);
        }
        return null;
    }

    @Override
    public boolean updateResume(Resume resume) {

        String sql = """
            UPDATE resume
            SET objective=?, education=?, experience=?,
                skills=?, projects=?
            WHERE seeker_id=?
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, resume.getObjective());
            ps.setString(2, resume.getEducation());
            ps.setString(3, resume.getExperience());
            ps.setString(4, resume.getSkills());
            ps.setString(5, resume.getProjects());
            ps.setLong(6, resume.getSeekerId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Update resume failed", e);
            return false;
        }
    }

    @Override
    public boolean updateResumeProfile(
            long seekerId,
            String education,
            String experience,
            String skills,
            String projects) {

        String sql = """
        UPDATE resume
        SET education=?,
            experience=?,
            skills=?,
            projects=?
        WHERE seeker_id=?
    """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, education);
            ps.setString(2, experience);
            ps.setString(3, skills);
            ps.setString(4, projects);
            ps.setLong(5, seekerId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Update resume profile failed", e);
            return false;
        }
    }

    @Override
    public Resume findBySeekerId(long seekerId) {

        String sql = "SELECT * FROM resume WHERE seeker_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Resume r = new Resume();
                r.setResumeId(rs.getLong("resume_id"));
                r.setSeekerId(seekerId);
                r.setEducation(rs.getString("education"));
                r.setExperience(rs.getString("experience"));
                r.setSkills(rs.getString("skills"));
                r.setProjects(rs.getString("projects"));
                return r;
            }

        } catch (SQLException e) {
            logger.error("Fetch resume failed", e);
        }
        return null;
    }

    @Override
    public boolean resumeExistsForSeeker(long seekerId) {

        String sql = "SELECT 1 FROM resume WHERE seeker_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, seekerId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }


}

