package org.jobportal.dao;

import org.jobportal.config.DBConnection;
import org.jobportal.model.JobSeekerProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JobSeekerDaoImpl implements JobSeekerDao {

    private static final Logger logger =
            LoggerFactory.getLogger(JobSeekerDaoImpl.class);

    @Override
    public boolean createProfile(JobSeekerProfile profile) {

        String sql = """
            INSERT INTO job_seeker_profile
            (seeker_id, first_name, last_name, phone,
             total_experience, location, profile_completed)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, profile.getSeekerId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setInt(5, profile.getTotalExperience());
            ps.setString(6, profile.getLocation());
            ps.setBoolean(7, true);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Create job seeker profile failed", e);
            return false;
        }
    }

    @Override
    public JobSeekerProfile getProfileBySeekerId(long seekerId) {

        String sql = "SELECT * FROM job_seeker_profile WHERE seeker_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JobSeekerProfile profile = new JobSeekerProfile();
                profile.setSeekerId(seekerId);
                profile.setFirstName(rs.getString("first_name"));
                profile.setLastName(rs.getString("last_name"));
                profile.setPhone(rs.getString("phone"));
                profile.setTotalExperience(rs.getInt("total_experience"));
                profile.setLocation(rs.getString("location"));
                profile.setProfileCompleted(rs.getBoolean("profile_completed"));
                return profile;
            }

        } catch (SQLException e) {
            logger.error("Fetch job seeker profile failed", e);
        }
        return null;
    }

    @Override
    public boolean updateProfile(JobSeekerProfile profile) {

        String sql = """
            UPDATE job_seeker_profile
            SET first_name=?, last_name=?, phone=?,
                total_experience=?, location=?, profile_completed=?
            WHERE seeker_id=?
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getPhone());
            ps.setInt(4, profile.getTotalExperience());
            ps.setString(5, profile.getLocation());
            ps.setBoolean(6, true);
            ps.setLong(7, profile.getSeekerId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Update job seeker profile failed", e);
            return false;
        }
    }


    @Override
    public boolean profileExists(long seekerId) {

        String sql = "SELECT 1 FROM job_seeker_profile WHERE seeker_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, seekerId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateProfileCompletionStatus(long seekerId, boolean completed) {

        String sql =
                "UPDATE job_seeker_profile SET profile_completed=? WHERE seeker_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, completed);
            ps.setLong(2, seekerId);
            ps.executeUpdate();

        } catch (Exception e) {

        }
    }


}

