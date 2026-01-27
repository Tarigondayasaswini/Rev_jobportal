package org.jobportal.controller;

import org.jobportal.model.Resume;
import org.jobportal.model.JobSeekerProfile;
import org.jobportal.service.JobSeekerService;
import org.jobportal.service.JobSeekerServiceImpl;
import org.jobportal.service.ResumeService;
import org.jobportal.service.ResumeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ResumeController {

    private static final Logger logger =
            LoggerFactory.getLogger(ResumeController.class);

    private static final ResumeService resumeService =
            new ResumeServiceImpl();

    private static final JobSeekerService jobSeekerService =
            new JobSeekerServiceImpl();

    /* ---------------- CREATE RESUME ---------------- */

    public static void createResume(long seekerId) {

        // 🔒 IMPORTANT FK GUARD
        JobSeekerProfile profile =
                jobSeekerService.viewProfile(seekerId);

        if (profile == null) {
            logger.error(
                    "Please create Job Seeker Profile before creating Resume"
            );
            return;
        }

        Scanner sc = new Scanner(System.in);
        Resume resume = new Resume();
        resume.setSeekerId(seekerId);

        logger.info("Enter Objective:");
        resume.setObjective(sc.nextLine());

        logger.info("Enter Education:");
        resume.setEducation(sc.nextLine());

        logger.info("Enter Experience:");
        resume.setExperience(sc.nextLine());

        logger.info("Enter Skills (comma separated):");
        resume.setSkills(sc.nextLine());

        logger.info("Enter Projects:");
        resume.setProjects(sc.nextLine());

        if (resumeService.createResume(resume)) {
            logger.info("Resume created successfully");
        } else {
            logger.error("Failed to create resume");
        }
    }

    /* ---------------- VIEW RESUME ---------------- */

    public static void viewResume(long seekerId) {

        Resume resume = resumeService.viewResume(seekerId);

        if (resume == null) {
            logger.info("Resume not found");
            return;
        }

        logger.info("Objective: {}", resume.getObjective());
        logger.info("Education: {}", resume.getEducation());
        logger.info("Experience: {}", resume.getExperience());
        logger.info("Skills: {}", resume.getSkills());
        logger.info("Projects: {}", resume.getProjects());
    }

    /* ---------------- UPDATE RESUME ---------------- */

    public static void updateResume(long seekerId) {

        Scanner sc = new Scanner(System.in);
        Resume resume = new Resume();
        resume.setSeekerId(seekerId);

        logger.info("Enter Objective:");
        resume.setObjective(sc.nextLine());

        logger.info("Enter Education:");
        resume.setEducation(sc.nextLine());

        logger.info("Enter Experience:");
        resume.setExperience(sc.nextLine());

        logger.info("Enter Skills:");
        resume.setSkills(sc.nextLine());

        logger.info("Enter Projects:");
        resume.setProjects(sc.nextLine());

        if (resumeService.updateResume(resume)) {
            logger.info("Resume updated successfully");
        } else {
            logger.error("Resume update failed");
        }
    }

    /* ---------------- PROFILE (USING RESUME TABLE) ---------------- */

    public static void updateProfile(long seekerId) {

        Scanner sc = new Scanner(System.in);

        logger.info("Enter Education:");
        String education = sc.nextLine();

        logger.info("Enter Work Experience:");
        String experience = sc.nextLine();

        logger.info("Enter Skills:");
        String skills = sc.nextLine();

        logger.info("Enter Certifications (optional):");
        String certifications = sc.nextLine();

        boolean success = resumeService.updateProfile(
                seekerId,
                education,
                experience,
                skills,
                certifications
        );

        if (success) {
            logger.info("Profile updated successfully");
        } else {
            logger.error("Profile update failed");
        }
    }

    public static void viewProfile(long seekerId) {

        Resume r = resumeService.viewProfile(seekerId);

        if (r == null) {
            logger.info("No profile found");
            return;
        }

        logger.info("=== MY PROFILE ===");
        logger.info("Education: {}", r.getEducation());
        logger.info("Experience: {}", r.getExperience());
        logger.info("Skills: {}", r.getSkills());
        logger.info("Certifications: {}", r.getProjects());
    }
}
