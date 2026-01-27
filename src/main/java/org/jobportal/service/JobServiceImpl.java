package org.jobportal.service;

import org.jobportal.dao.JobDao;
import org.jobportal.dao.JobDaoImpl;
import org.jobportal.model.Job;

import java.util.List;

public class JobServiceImpl implements JobService {

    private final JobDao dao = new JobDaoImpl();

    @Override
    public long postJob(Job job) {
        return dao.createJob(job);
    }

    @Override
    public List<Job> viewCompanyJobs(long companyId) {
        return dao.getJobsByCompany(companyId);
    }

    @Override
    public List<Job> viewOpenJobs() {
        return dao.getOpenJobs();
    }

    @Override
    public boolean closeJob(long jobId) {
        return dao.closeJob(jobId);
    }

    @Override
    public boolean updateJob(Job job) {
        return dao.updateJob(job);
    }

    @Override
    public boolean reopenJob(long jobId) {
        return dao.reopenJob(jobId);
    }


    @Override
    public List<Job> searchJobsWithFilters(
            String title,
            String location,
            Integer experience,
            String companyName,
            Double minSalary,
            Double maxSalary,
            String jobType) {

        return dao.searchJobsWithFilters(
                title, location, experience,
                companyName, minSalary, maxSalary, jobType
        );
    }

    @Override
    public boolean deleteJob(long jobId) {
        return dao.deleteJob(jobId);
    }

    @Override
    public Object[] getJobStatistics(long jobId) {
        return dao.getJobStatistics(jobId);
    }


}

