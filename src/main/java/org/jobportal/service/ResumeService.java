package org.jobportal.service;

import org.jobportal.model.Resume;

public interface ResumeService {

    boolean createResume(Resume resume);

    Resume viewResume(long seekerId);

    boolean updateResume(Resume resume);

    boolean updateProfile(
            long seekerId,
            String education,
            String experience,
            String skills,
            String certifications
    );

    Resume viewProfile(long seekerId);

}
