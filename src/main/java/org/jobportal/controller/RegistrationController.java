package org.jobportal.controller;

import org.jobportal.model.PasswordRecovery;
import org.jobportal.model.User;
import org.jobportal.service.PasswordRecoveryService;
import org.jobportal.service.PasswordRecoveryServiceImpl;
import org.jobportal.service.UserService;
import org.jobportal.service.UserServiceImpl;
import org.jobportal.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class RegistrationController {

    private static final Logger logger =
            LoggerFactory.getLogger(RegistrationController.class);

    private static final PasswordRecoveryService passwordRecoveryService =
            new PasswordRecoveryServiceImpl();

    public static void register() {

        Scanner sc = new Scanner(System.in);
        UserService service = new UserServiceImpl();

        logger.info("Enter Email:");
        String email = sc.nextLine();

        logger.info("Enter Password:");
        String password = sc.nextLine();

        if (!InputValidator.isValidEmail(email)) {
            logger.error("Invalid email format. Example: user@gmail.com");
            return;
        }

        if (!InputValidator.isValidPassword(password)) {
            logger.error(
                    "Password must contain:\n" +
                            "- Minimum 8 characters\n" +
                            "- One uppercase letter\n" +
                            "- One lowercase letter\n" +
                            "- One number\n" +
                            "- One special character"
            );
            return;
        }

        logger.info("Enter Role (JOB_SEEKER / EMPLOYER):");
        String role = sc.nextLine().toUpperCase();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setCompanyId(null);

        if (!service.register(user)) {
            logger.error("Registration failed");
            return;
        }

        //  LOGIN AGAIN TO GET userId (CRITICAL FIX)
        User savedUser = service.login(email, password);

        if (savedUser == null) {
            logger.error("Registration failed during verification");
            return;
        }

        logger.info("Registration successful");
        logger.info("Set security question for password recovery");

        logger.info("Choose a security question:");
        logger.info("1. What is your pet name?");
        logger.info("2. What is your favorite color?");
        logger.info("3. What is your birth city?");

        int choice = sc.nextInt();
        sc.nextLine();

        String question = switch (choice) {
            case 1 -> "What is your pet name?";
            case 2 -> "What is your favorite color?";
            case 3 -> "What is your birth city?";
            default -> null;
        };

        if (question == null) {
            logger.error("Invalid choice. Security question not set.");
            return;
        }

        logger.info("Enter answer:");
        String answer = sc.nextLine();

        PasswordRecovery recovery = new PasswordRecovery();
        recovery.setUserId(savedUser.getUserId());
        recovery.setSecurityQuestion(question);
        recovery.setSecurityAnswer(answer);

        passwordRecoveryService.saveRecoveryDetails(recovery);

        logger.info("Security question saved successfully");
    }
}
