package org.jobportal.service;

import org.jobportal.model.Job;

import java.util.List;

public interface JobService {

    long postJob(Job job);

    List<Job> viewCompanyJobs(long companyId);

    List<Job> viewOpenJobs();

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


}

