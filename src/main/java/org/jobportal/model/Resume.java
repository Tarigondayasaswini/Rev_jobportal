package org.jobportal.model;

public class Resume {

    private long resumeId;
    private long seekerId;
    private String objective;
    private String education;
    private String experience;
    private String skills;
    private String projects;
    @SuppressWarnings("unused")

    public long getResumeId() {
        return resumeId;
    }

    public void setResumeId(long resumeId) {
        this.resumeId = resumeId;
    }

    public long getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(long seekerId) {
        this.seekerId = seekerId;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }
}

