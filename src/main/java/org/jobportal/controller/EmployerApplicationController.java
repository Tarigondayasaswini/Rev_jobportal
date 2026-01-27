
package org.jobportal.controller;

import org.jobportal.service.JobApplicationService;
import org.jobportal.service.JobApplicationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployerApplicationController {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployerApplicationController.class);

    private static final JobApplicationService service =
            new JobApplicationServiceImpl();

    public static void viewApplicantsByJob() {

        Scanner sc = new Scanner(System.in);
        logger.info("Enter Job ID:");
        long jobId = sc.nextLong();

        List<Object[]> list =
                service.viewApplicantsByJob(jobId);

        if (list.isEmpty()) {
            logger.info("No applicants found");
            return;
        }

        for (Object[] a : list) {
            logger.info(
                    "AppID:{} | Name:{} | Exp:{} | Edu:{} | Skills:{} | Date:{} | Status:{}",
                    a[0], a[1], a[2], a[3], a[4], a[5], a[6]
            );
        }
    }


    public static void filterApplicants() {

        Scanner sc = new Scanner(System.in);

        logger.info("Enter Job ID:");
        long jobId = sc.nextLong();
        sc.nextLine();

        logger.info("Min Experience (optional):");
        String exp = sc.nextLine();
        Integer minExp = exp.isEmpty() ? null : Integer.parseInt(exp);

        logger.info("Skills (optional):");
        String skills = sc.nextLine();
        if (skills.isEmpty()) skills = null;

        logger.info("Education (optional):");
        String edu = sc.nextLine();
        if (edu.isEmpty()) edu = null;

        List<Object[]> list =
                service.filterApplicants(jobId, minExp, skills, edu, null);

        list.forEach(a ->
                logger.info("AppID:{} | {} | Exp:{} | Edu:{} | Skills:{} | Date:{}",
                        a[0], a[1], a[2], a[3], a[4], a[5])
        );
    }
    public static void bulkUpdateStatus() {

        Scanner sc = new Scanner(System.in);

        logger.info("Enter Job ID:");
        long jobId = sc.nextLong();
        sc.nextLine();

        logger.info("Enter Application IDs (comma separated):");
        String[] ids = sc.nextLine().split(",");

        List<Long> applicationIds = new ArrayList<>();
        for (String id : ids) {
            applicationIds.add(Long.parseLong(id.trim()));
        }

        logger.info("Enter Status (SHORTLISTED / REJECTED):");
        String status = sc.nextLine().toUpperCase();

        logger.info("Enter comment (optional):");
        String comment = sc.nextLine();

        boolean success =
                service.bulkShortlistOrReject(
                        applicationIds, status, comment
                );

        if (success) {
            logger.info("Bulk update successful");
        } else {
            logger.error("Bulk update failed");
        }
    }

}
