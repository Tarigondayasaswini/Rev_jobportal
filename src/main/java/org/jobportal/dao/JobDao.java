package org.jobportal.dao;

import org.jobportal.model.Job;

import java.util.List;

public interface JobDao {

    long createJob(Job job);

    List<Job> getJobsByCompany(long companyId);

    List<Job> getOpenJobs();

    boolean closeJob(long jobId);

    boolean updateJob(Job job);
    boolean reopenJob(long jobId);
    boolean deleteJob(long jobId);

    Object[] getJobStatistics(long jobId);


    List<Job> searchJobsWithFilters(
            String title,
            String location,
            Integer experience,
            String companyName,
            Double minSalary,
            Double maxSalary,
            String jobType
    );

    boolean existsById(long jobId);

    boolean isJobOpen(long jobId);


}
