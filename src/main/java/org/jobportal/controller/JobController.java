package org.jobportal.controller;

import org.jobportal.model.Job;
import org.jobportal.service.JobApplicationService;
import org.jobportal.service.JobApplicationServiceImpl;
import org.jobportal.service.JobService;
import org.jobportal.service.JobServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class JobController {

    private static final Logger logger =
            LoggerFactory.getLogger(JobController.class);

    private static final JobService service =
            new JobServiceImpl();

    private static final JobApplicationService jobApplicationService =
            new JobApplicationServiceImpl();

    // 🔥 SINGLE SCANNER
    private static final Scanner sc = new Scanner(System.in);

    // ---------------- COMMON INPUT METHODS ----------------

    private static long readJobId(String message) {
        logger.info(message);
        return sc.nextLong();
    }

    private static Job readJobDetails(long companyId, boolean includeJobId) {

        Job job = new Job();

        if (includeJobId) {
            logger.info("Enter Job ID:");
            job.setJobId(sc.nextLong());
            sc.nextLine();
        }

        job.setCompanyId(companyId);

        logger.info("Enter Job Title:");
        job.setTitle(sc.nextLine());

        logger.info("Enter Description:");
        job.setDescription(sc.nextLine());

        logger.info("Enter Skills:");
        job.setSkills(sc.nextLine());

        logger.info("Enter Experience (years):");
        job.setExperience(sc.nextInt());
        sc.nextLine();

        logger.info("Enter Education:");
        job.setEducation(sc.nextLine());

        logger.info("Enter Location:");
        job.setLocation(sc.nextLine());

        logger.info("Enter Salary Min:");
        job.setSalaryMin(sc.nextBigDecimal());

        logger.info("Enter Salary Max:");
        job.setSalaryMax(sc.nextBigDecimal());
        sc.nextLine();

        logger.info("Enter Job Type:");
        job.setJobType(sc.nextLine());

        logger.info("Enter Deadline (YYYY-MM-DD):");
        job.setDeadline(LocalDate.parse(sc.nextLine()));

        return job;
    }

    // ---------------- CONTROLLER METHODS ----------------

    public static void postJob(long companyId) {

        Job job = readJobDetails(companyId, false);
        long jobId = service.postJob(job);

        if (jobId > 0) {
            logger.info("Job posted successfully with ID {}", jobId);
            jobApplicationService.notifyJobMatches(
                    job.getSkills(),
                    job.getLocation()
            );
        } else {
            logger.error("Job posting failed");
        }
    }

    public static void viewCompanyJobs(long companyId) {

        List<Job> jobs = service.viewCompanyJobs(companyId);

        if (jobs.isEmpty()) {
            logger.info("No jobs found");
            return;
        }

        jobs.forEach(job ->
                logger.info("ID:{} | {} | {}",
                        job.getJobId(),
                        job.getTitle(),
                        job.getStatus()));
    }

    public static void closeJob() {

        long jobId = readJobId("Enter Job ID to close:");

        if (service.closeJob(jobId)) {
            logger.info("Job closed successfully");
        } else {
            logger.error("Failed to close job");
        }
    }

    public static void searchJobs() {

        logger.info("Enter Job Title (optional):");
        String title = readOptionalString();

        logger.info("Enter Location (optional):");
        String location = readOptionalString();

        logger.info("Enter Experience (optional):");
        Integer experience = readOptionalInteger();

        logger.info("Enter Company Name (optional):");
        String company = readOptionalString();

        logger.info("Enter Min Salary (optional):");
        Double minSalary = readOptionalDouble();

        logger.info("Enter Max Salary (optional):");
        Double maxSalary = readOptionalDouble();

        logger.info("Enter Job Type (optional):");
        String jobType = readOptionalString();

        List<Job> jobs = service.searchJobsWithFilters(
                title, location, experience,
                company, minSalary, maxSalary, jobType
        );

        if (jobs.isEmpty()) {
            logger.info("No jobs found");
        } else {
            jobs.forEach(j ->
                    logger.info("JobID:{} | {} | {} | {}",
                            j.getJobId(),
                            j.getTitle(),
                            j.getLocation(),
                            j.getJobType()));
        }
    }

    public static void editJob() {

        Job job = readJobDetails(0, true);

        if (service.updateJob(job)) {
            logger.info("Job updated successfully");
        } else {
            logger.error("Job update failed");
        }
    }

    public static void reopenJob() {

        long jobId = readJobId("Enter Job ID to reopen:");

        if (service.reopenJob(jobId)) {
            logger.info("Job reopened successfully");
        } else {
            logger.error("Failed to reopen job");
        }
    }

    public static void deleteJob() {

        long jobId = readJobId("Enter Job ID to delete:");

        if (service.deleteJob(jobId)) {
            logger.info("Job deleted successfully");
        } else {
            logger.error(
                    "Job deletion failed. Either job does not exist or applications already exist."
            );
        }
    }

    public static void viewJobStatistics() {

        long jobId = readJobId("Enter Job ID:");

        Object[] stats = service.getJobStatistics(jobId);

        if (stats == null) {
            logger.error("No statistics found");
            return;
        }

        logger.info("Total Applications: {}", stats[0]);
        logger.info("Shortlisted: {}", stats[1]);
        logger.info("Rejected: {}", stats[2]);
    }

    // ---------------- OPTIONAL INPUT HELPERS ----------------

    private static String readOptionalString() {
        String value = sc.nextLine().trim();
        return value.isEmpty() ? null : value;
    }

    private static Integer readOptionalInteger() {
        String value = sc.nextLine().trim();
        return value.isEmpty() ? null : Integer.parseInt(value);
    }

    private static Double readOptionalDouble() {
        String value = sc.nextLine().trim();
        return value.isEmpty() ? null : Double.parseDouble(value);
    }
}
