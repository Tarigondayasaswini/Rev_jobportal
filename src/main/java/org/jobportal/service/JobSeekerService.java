package org.jobportal.service;

import org.jobportal.model.JobSeekerProfile;

public interface JobSeekerService {

    boolean createProfile(JobSeekerProfile profile);

    JobSeekerProfile viewProfile(long seekerId);

    boolean updateProfile(JobSeekerProfile profile);

    void updateProfileCompletionStatus(long seekerId);

}

