package org.jobportal.service;

import org.jobportal.model.JobApplication;
import org.jobportal.util.ApplyStatus;

import java.util.List;

public interface JobApplicationService {


    List<JobApplication> getMyApplications(long seekerId);



    ApplyStatus applyWithSelectedResume(
            long jobId,
            long seekerId,
            long resumeId,
            String coverLetter
    );

    ApplyStatus applyWithLatestResume(
            long jobId,
            long seekerId,
            String coverLetter
    );
    List<Object[]> viewMyApplications(long seekerId);

    boolean withdrawApplication(
            long applicationId,
            long seekerId,
            String reason
    );

    List<Object[]> viewApplicantsByJob(long jobId);


    List<Object[]> filterApplicants(
            long jobId,
            Integer experience,
            String skills,
            String education,
            java.time.LocalDate appliedAfter
    );

    boolean bulkShortlistOrReject(
            List<Long> applicationIds,
            String status,
            String comment
    );

    void notifyJobMatches(String skills, String location);

}

