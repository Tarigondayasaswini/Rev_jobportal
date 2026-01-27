package org.jobportal.model;

import java.time.LocalDateTime;

public class JobApplication {

    private long applicationId;
    private long jobId;
    private long seekerId;
    private long resumeId;
    private String coverLetter;
    private String status;      // APPLIED, SHORTLISTED, REJECTED, WITHDRAWN
    private LocalDateTime appliedAt;
    private String jobTitle;
    private String companyName;
    @SuppressWarnings("unused")
    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(long seekerId) {
        this.seekerId = seekerId;
    }

    public long getResumeId() {
        return resumeId;
    }

    public void setResumeId(long resumeId) {
        this.resumeId = resumeId;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @SuppressWarnings("unused")
    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}



