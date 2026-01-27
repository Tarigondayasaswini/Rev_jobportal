package org.jobportal.model;

public class JobSeekerProfile {

    private long seekerId; // same as users.user_id
    private String firstName;
    private String lastName;
    private String phone;
    private int totalExperience;
    private String location;
    private boolean profileCompleted;

    public long getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(long seekerId) {
        this.seekerId = seekerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }
}

