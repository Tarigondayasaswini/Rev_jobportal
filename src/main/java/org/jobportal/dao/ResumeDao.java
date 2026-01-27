package org.jobportal.dao;

import org.jobportal.model.Resume;

public interface ResumeDao {

    boolean createResume(Resume resume);
    boolean resumeExistsForSeeker(long seekerId);


    Resume getResumeBySeekerId(long seekerId);

    boolean updateResume(Resume resume);
    boolean updateResumeProfile(
            long seekerId,
            String education,
            String experience,
            String skills,
            String projects
    );

    Resume findBySeekerId(long seekerId);


}

