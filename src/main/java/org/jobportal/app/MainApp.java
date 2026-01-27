package org.jobportal.app;

import org.jobportal.config.DBConnection;
import org.jobportal.model.User;
import org.jobportal.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class MainApp {
    private static User loggedInUser;


    private static final Logger logger =
            LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            logger.info("=== JOB PORTAL CONSOLE APPLICATION ===");
            logger.info("1. Register");
            logger.info("2. Login");
            logger.info("3. Forgot Password");
            logger.info("0. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1 -> RegistrationController.register();
                case 2 -> handleLogin();
                case 3 -> ForgotPasswordController.resetPassword();

                case 0 -> {
                    logger.info("Thank you for using Job Portal");
                    DBConnection.closeConnection();
                    System.exit(0);
                }

                default -> logger.error("Invalid choice");
            }
        }
    }

    /* ---------------- LOGIN HANDLER ---------------- */

    private static void handleLogin() {

        loggedInUser = LoginController.login();
        if (loggedInUser == null) return;

        logger.info("Welcome {}", loggedInUser.getEmail());

        if ("JOB_SEEKER".equals(loggedInUser.getRole())) {
            jobSeekerMenu(loggedInUser);
        } else {
            employerMenu(loggedInUser);
        }

    }

    /* ---------------- JOB SEEKER MENU ---------------- */

    private static void jobSeekerMenu(User user) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            logger.info("=== JOB SEEKER MENU ===");
            logger.info("1. Create Profile");
            logger.info("2. View Profile");
            logger.info("3. Create Resume");
            logger.info("4. View Resume");
            logger.info("5. Update Resume");
            logger.info("6. Search Jobs");
            logger.info("7. Apply for Job");
            logger.info("8. View My Applications");
            logger.info("9. Withdraw Application");
            logger.info("10. View Notifications");
            logger.info("11. Change Password");
            logger.info("0. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1 -> JobSeekerController.createProfile(user.getUserId());
                case 2 -> JobSeekerController.viewProfile(user.getUserId());
                case 3 -> ResumeController.createResume(user.getUserId());
                case 4 -> ResumeController.viewResume(user.getUserId());
                case 5 -> ResumeController.updateResume(user.getUserId());
                case 6 -> JobController.searchJobs();
                case 7 -> JobApplicationController.applyForJob(user.getUserId());
                case 8 -> JobApplicationController.viewMyApplications(user.getUserId());
                case 9 -> JobApplicationController.withdrawApplication(user.getUserId());
                case 10 -> NotificationController.viewMyNotifications(user.getUserId());
                case 11 -> ChangePasswordController.changePassword(user);

                case 0 -> {
                    logger.info("Logged out successfully");
                    return;
                }

                default -> logger.error("Invalid choice");
            }
        }
    }


    /* ---------------- EMPLOYER MENU ---------------- */

    private static void employerMenu(User user) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            logger.info("=== EMPLOYER MENU ===");
            logger.info("1. Create Company");
            logger.info("2. View Company");
            logger.info("3. Update Company");
            logger.info("4. Post Job");
            logger.info("5. View Company Jobs");
            logger.info("6. Close Job");
            logger.info("7. Reopen Job");
            logger.info("8. Edit Job");
            logger.info("9. Delete Job");
            logger.info("10. View Applicants");
            logger.info("11. Filter Applicants");
            logger.info("12. Bulk Shortlist / Reject");
            logger.info("13. Job Statistics");
            logger.info("14. Change Password");
            logger.info("0. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1 ->
                        CompanyController.createCompany(
                                loggedInUser.getUserId()
                        );

                case 2 -> CompanyController.viewCompany(user.getCompanyId());
                case 3 -> CompanyController.updateCompany(user.getCompanyId());
                case 4 -> JobController.postJob(user.getCompanyId());
                case 5 -> JobController.viewCompanyJobs(user.getCompanyId());
                case 6 -> JobController.closeJob();
                case 7 -> JobController.reopenJob();
                case 8 -> JobController.editJob();
                case 9 -> JobController.deleteJob();
                case 10 -> EmployerApplicationController.viewApplicantsByJob();
                case 11 -> EmployerApplicationController.filterApplicants();
                case 12 -> EmployerApplicationController.bulkUpdateStatus();
                case 13 -> JobController.viewJobStatistics();
                case 14 -> ChangePasswordController.changePassword(user);

                case 0 -> {
                    logger.info("Logged out successfully");
                    return;
                }

                default -> logger.error("Invalid choice");
            }
        }
    }

    public static void setLoggedInUserCompanyId(long companyId) {
        if (loggedInUser != null) {
            loggedInUser.setCompanyId(companyId);
        }
    }

}
