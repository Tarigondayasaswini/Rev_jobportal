package org.jobportal.service;

import org.jobportal.dao.JobApplicationDao;
import org.jobportal.dao.JobApplicationDaoImpl;
import org.jobportal.dao.JobDao;
import org.jobportal.dao.JobDaoImpl;
import org.jobportal.model.JobApplication;
import org.jobportal.model.Notification;
import org.jobportal.util.ApplyStatus;

import java.time.LocalDate;
import java.util.List;

public class JobApplicationServiceImpl
        implements JobApplicationService {

    private final JobApplicationDao dao =
            new JobApplicationDaoImpl();
    private final JobDao jobDao = new JobDaoImpl();

    private final NotificationService notificationService =
            new NotificationServiceImpl();

    @Override
    public List<JobApplication> getMyApplications(long seekerId) {
        return dao.findBySeeker(seekerId);
    }

    // ---------------- APPLY WITH SELECTED RESUME ----------------

    @Override
    public ApplyStatus applyWithSelectedResume(
            long jobId,
            long seekerId,
            long resumeId,
            String coverLetter) {

        if (resumeId <= 0) {
            return ApplyStatus.RESUME_NOT_FOUND;
        }

        return applyInternal(
                jobId,
                seekerId,
                resumeId,
                coverLetter
        );
    }

    // ---------------- APPLY WITH LATEST RESUME ----------------

    @Override
    public ApplyStatus applyWithLatestResume(
            long jobId,
            long seekerId,
            String coverLetter) {

        Long resumeId = dao.findLatestResumeId(seekerId);
        if (resumeId == null) {
            return ApplyStatus.RESUME_NOT_FOUND;
        }

        return applyInternal(
                jobId,
                seekerId,
                resumeId,
                coverLetter
        );
    }

    // ---------------- COMMON APPLY LOGIC ----------------

    private ApplyStatus applyInternal(
            long jobId,
            long seekerId,
            long resumeId,
            String coverLetter) {

        if (!jobDao.existsById(jobId)) {
            return ApplyStatus.JOB_NOT_FOUND;
        }

        if (!jobDao.isJobOpen(jobId)) {
            return ApplyStatus.JOB_CLOSED;
        }

        if (dao.hasAppliedForJob(jobId, seekerId)) {
            return ApplyStatus.DUPLICATE_APPLY;
        }

        JobApplication application = new JobApplication();
        application.setJobId(jobId);
        application.setSeekerId(seekerId);
        application.setResumeId(resumeId);
        application.setCoverLetter(coverLetter);

        return dao.save(application)
                ? ApplyStatus.SUCCESS
                : ApplyStatus.FAILED;
    }

    // ---------------- OTHER METHODS ----------------

    @Override
    public List<Object[]> viewMyApplications(long seekerId) {
        return dao.findApplicationsWithJobDetails(seekerId);
    }

    @Override
    public boolean withdrawApplication(
            long applicationId,
            long seekerId,
            String reason) {

        return dao.withdraw(applicationId, seekerId);
    }

    @Override
    public List<Object[]> viewApplicantsByJob(long jobId) {
        return dao.findApplicantsByJob(jobId);
    }

    @Override
    public List<Object[]> filterApplicants(
            long jobId,
            Integer experience,
            String skills,
            String education,
            LocalDate appliedAfter) {

        java.sql.Date sqlDate =
                (appliedAfter == null)
                        ? null
                        : java.sql.Date.valueOf(appliedAfter);

        return dao.filterApplicants(
                jobId,
                experience,
                skills,
                education,
                sqlDate
        );
    }

    @Override
    public boolean bulkShortlistOrReject(
            List<Long> applicationIds,
            String status,
            String comment) {

        boolean updated =
                dao.bulkUpdateStatus(applicationIds, status, comment);

        if (updated) {
            for (Long appId : applicationIds) {
                long seekerId =
                        dao.findSeekerIdByApplication(appId);

                Notification n = new Notification();
                n.setUserId(seekerId);
                n.setMessage(
                        "Your application was " + status
                );

                notificationService.notifyUser(n);
            }
        }
        return updated;
    }

    @Override
    public void notifyJobMatches(String skills, String location) {

        List<Long> seekers =
                dao.findMatchingSeekers(skills, location);

        for (Long seekerId : seekers) {

            Notification n = new Notification();
            n.setUserId(seekerId);
            n.setMessage(
                    "New job matching your profile is available"
            );

            notificationService.notifyUser(n);
        }
    }
}
