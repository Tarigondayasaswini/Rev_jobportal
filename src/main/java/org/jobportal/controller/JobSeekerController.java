package org.jobportal.controller;

import org.jobportal.model.JobSeekerProfile;
import org.jobportal.service.JobSeekerService;
import org.jobportal.service.JobSeekerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class JobSeekerController {

    private static final Logger logger =
            LoggerFactory.getLogger(JobSeekerController.class);

    private static final JobSeekerService service =
            new JobSeekerServiceImpl();

    public static void createProfile(long userId) {

        Scanner sc = new Scanner(System.in);
        JobSeekerProfile profile = new JobSeekerProfile();

        profile.setSeekerId(userId);

        logger.info("Enter First Name:");
        profile.setFirstName(sc.nextLine());

        logger.info("Enter Last Name:");
        profile.setLastName(sc.nextLine());

        logger.info("Enter Phone:");
        profile.setPhone(sc.nextLine());

        logger.info("Enter Total Experience:");
        profile.setTotalExperience(sc.nextInt());
        sc.nextLine();

        logger.info("Enter Location:");
        profile.setLocation(sc.nextLine());

        if (service.createProfile(profile)) {
            logger.info("Job seeker profile created successfully");
        } else {
            logger.error("Failed to create profile");
        }
    }

    public static void viewProfile(long userId) {

        JobSeekerProfile profile = service.viewProfile(userId);

        if (profile == null) {
            logger.info("Profile not found");
            return;
        }

        logger.info("First Name: {}", profile.getFirstName());
        logger.info("Last Name: {}", profile.getLastName());
        logger.info("Phone: {}", profile.getPhone());
        logger.info("Experience: {}", profile.getTotalExperience());
        logger.info("Location: {}", profile.getLocation());
    }
}
