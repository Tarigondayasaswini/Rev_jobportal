package org.jobportal.controller;

import org.jobportal.model.PasswordRecovery;
import org.jobportal.model.User;
import org.jobportal.service.PasswordRecoveryService;
import org.jobportal.service.PasswordRecoveryServiceImpl;
import org.jobportal.service.UserService;
import org.jobportal.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ForgotPasswordController {

    private static final Logger logger =
            LoggerFactory.getLogger(ForgotPasswordController.class);

    public static void resetPassword() {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserServiceImpl();
        PasswordRecoveryService recoveryService =
                new PasswordRecoveryServiceImpl();

        logger.info("Enter registered email:");
        String email = sc.nextLine();

        // find user by email
        User user = userService.findByEmail(email);

        if (user == null) {
            logger.error("Email not found");
            return;
        }

        //1. fetch security question
        PasswordRecovery recovery =
                recoveryService.getRecoveryDetails(user.getUserId());

        if (recovery == null) {
            logger.error("Security question not set for this account");
            return;
        }

        logger.info("Security Question: {}", recovery.getSecurityQuestion());

        // 3 validate answer
        logger.info("Enter answer:");
        String answer = sc.nextLine();

        if (!recovery.getSecurityAnswer().equals(answer)) {
            logger.error("Incorrect answer");
            return;
        }

        // 4️ reset password
        logger.info("Enter new password:");
        String newPassword = sc.nextLine();

        boolean success =
                userService.updatePasswordByUserId(
                        user.getUserId(), newPassword
                );

        if (success) {
            logger.info("Password reset successful");
        } else {
            logger.error("Password reset failed");
        }
    }
}
