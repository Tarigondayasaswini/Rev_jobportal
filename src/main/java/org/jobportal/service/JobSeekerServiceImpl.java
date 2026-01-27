package org.jobportal.service;

import org.jobportal.dao.JobSeekerDao;
import org.jobportal.dao.JobSeekerDaoImpl;
import org.jobportal.model.JobSeekerProfile;
import org.jobportal.dao.ResumeDao;
import org.jobportal.dao.ResumeDaoImpl;

public class JobSeekerServiceImpl implements JobSeekerService {

    private final JobSeekerDao dao = new JobSeekerDaoImpl();
    private final JobSeekerDao jobSeekerDao = new JobSeekerDaoImpl();
    private final ResumeDao resumeDao = new ResumeDaoImpl();


    @Override
    public boolean createProfile(JobSeekerProfile profile) {
        return dao.createProfile(profile);
    }

    @Override
    public JobSeekerProfile viewProfile(long seekerId) {
        return dao.getProfileBySeekerId(seekerId);
    }

    @Override
    public boolean updateProfile(JobSeekerProfile profile) {
        return dao.updateProfile(profile);
    }


    @Override
    public void updateProfileCompletionStatus(long seekerId) {

        boolean hasProfile = jobSeekerDao.profileExists(seekerId);
        boolean hasResume = resumeDao.resumeExistsForSeeker(seekerId);

        boolean completed = hasProfile && hasResume;

        jobSeekerDao.updateProfileCompletionStatus(seekerId, completed);
    }



}

