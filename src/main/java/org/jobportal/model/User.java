
package org.jobportal.model;

public class User {

    private long userId;
    private String email;
    private String password;
    private String role;          // JOB_SEEKER / EMPLOYER
    private Long companyId;       // nullable
    private boolean isActive;

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}


