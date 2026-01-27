package org.jobportal.controller;

import org.jobportal.service.JobApplicationService;
import org.jobportal.service.JobApplicationServiceImpl;
import org.jobportal.util.ApplyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class JobApplicationController {

    private static final Logger logger =
            LoggerFactory.getLogger(JobApplicationController.class);

    private static final JobApplicationService service =
            new JobApplicationServiceImpl();

    public static void applyForJob(long seekerId) {

        Scanner sc = new Scanner(System.in);

        logger.info("Enter Job ID:");
        long jobId = sc.nextLong();
        sc.nextLine();

        logger.info("1. One-click apply (default saved resume)");
        logger.info("2. Apply using selected resume");

        int choice = sc.nextInt();
        sc.nextLine();

        logger.info("Enter cover letter (optional):");
        String coverLetter = sc.nextLine();
        if (coverLetter.isBlank()) coverLetter = null;

        ApplyStatus status;

        if (choice == 1) {
            status = service.applyWithLatestResume(
                    jobId, seekerId, coverLetter
            );
        } else {
            logger.info("Enter Resume ID:");
            long resumeId = sc.nextLong();
            sc.nextLine();

            status = service.applyWithSelectedResume(
                    jobId, seekerId, resumeId, coverLetter
            );
        }

        // PROPER MESSAGE HANDLING
        switch (status) {

            case SUCCESS ->
                    logger.info("Job applied successfully");

            case JOB_NOT_FOUND ->
                    logger.error("Job not found with given ID");

            case JOB_CLOSED ->
                    logger.error("Job is closed and not accepting applications");

            case RESUME_NOT_FOUND ->
                    logger.error("Resume not found. Please create a resume first");

            case DUPLICATE_APPLY ->
                    logger.error("You have already applied for this job");

            default ->
                    logger.error("Application failed due to system error");
        }
    }

    public static void viewMyApplications(long seekerId) {

        List<Object[]> applications =
                service.viewMyApplications(seekerId);

        if (applications.isEmpty()) {
            logger.info("No applications found");
            return;
        }

        logger.info("=== MY JOB APPLICATIONS ===");

        for (Object[] row : applications) {
            logger.info(
                    "Application ID: {} | Job: {} | Company: {} | Location: {} | Status: {} | Applied On: {}",
                    row[0], row[1], row[2], row[3], row[4], row[5]
            );
        }

        logger.info("-------------------------------------");
        logger.info("Use Application ID to withdraw an application");
    }

    public static void withdrawApplication(long seekerId) {

        Scanner sc = new Scanner(System.in);

        logger.info("Enter Application ID to withdraw:");
        long applicationId = sc.nextLong();
        sc.nextLine();

        logger.info("Are you sure you want to withdraw? (yes/no)");
        String confirm = sc.nextLine();

        if (!"yes".equalsIgnoreCase(confirm)) {
            logger.info("Withdrawal cancelled");
            return;
        }

        logger.info("Enter reason (optional):");
        String reason = sc.nextLine();

        boolean success = service.withdrawApplication(
                applicationId,
                seekerId,
                reason
        );

        if (success) {
            logger.info("Application withdrawn successfully");
        } else {
            logger.error("Withdraw failed (invalid application)");
        }
    }
}
