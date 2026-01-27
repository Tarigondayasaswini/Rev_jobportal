package org.jobportal.dao;

import org.jobportal.model.JobSeekerProfile;

public interface JobSeekerDao {
    boolean profileExists(long seekerId);

    void updateProfileCompletionStatus(long seekerId, boolean completed);


    boolean createProfile(JobSeekerProfile profile);

    JobSeekerProfile getProfileBySeekerId(long seekerId);

    boolean updateProfile(JobSeekerProfile profile);


}

