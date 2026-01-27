package org.jobportal.dao;

import org.jobportal.model.JobApplication;

import java.util.List;
import java.sql.Date;

public interface JobApplicationDao {

    boolean save(JobApplication application);

    List<JobApplication> findBySeeker(long seekerId);


    Long findLatestResumeId(long seekerId);

    List<Object[]> findApplicationsWithJobDetails(long seekerId);

    boolean withdraw(long applicationId, long seekerId);

    long findSeekerIdByApplication(long applicationId);
    List<Object[]> findApplicantsByJob(long jobId);

    List<Object[]> filterApplicants(
            long jobId,
            Integer minExperience,
            String skills,
            String education,
            Date appliedAfter
    );

    boolean bulkUpdateStatus(
            List<Long> applicationIds,
            String status,
            String comment
    );

    Object[] getJobStatistics(long jobId);

    List<Long> findMatchingSeekers(
            String skills,
            String location
    );

    boolean hasAppliedForJob(long jobId, long seekerId);

}


