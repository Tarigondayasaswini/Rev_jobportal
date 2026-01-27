package org.jobportal.service;

import org.jobportal.dao.ResumeDao;
import org.jobportal.dao.ResumeDaoImpl;
import org.jobportal.model.Resume;

public class ResumeServiceImpl implements ResumeService {

    private final ResumeDao dao = new ResumeDaoImpl();

    @Override
    public boolean createResume(Resume resume) {
        return dao.createResume(resume);
    }

    @Override
    public Resume viewResume(long seekerId) {
        return dao.getResumeBySeekerId(seekerId);
    }

    @Override
    public boolean updateResume(Resume resume) {
        return dao.updateResume(resume);
    }


    @Override
    public boolean updateProfile(
            long seekerId,
            String education,
            String experience,
            String skills,
            String certifications) {

        return dao.updateResumeProfile(
                seekerId,
                education,
                experience,
                skills,
                certifications
        );
    }

    @Override
    public Resume viewProfile(long seekerId) {
        return dao.findBySeekerId(seekerId);
    }

}
