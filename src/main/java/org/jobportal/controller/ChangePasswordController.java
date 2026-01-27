package org.jobportal.controller;

import org.jobportal.model.User;
import org.jobportal.service.UserService;
import org.jobportal.service.UserServiceImpl;
import org.jobportal.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ChangePasswordController {

    private static final Logger logger =
            LoggerFactory.getLogger(ChangePasswordController.class);

    public static void changePassword(User user) {

        Scanner sc = new Scanner(System.in);
        UserService service = new UserServiceImpl();

        logger.info("Enter current password:");
        String currentPassword = sc.nextLine();

        logger.info("Enter new password:");
        String newPassword = sc.nextLine();
        if (!InputValidator.isValidPassword(newPassword)) {
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


        boolean success =
                service.changePassword(
                        user.getUserId(),
                        currentPassword,
                        newPassword
                );

        if (success) {
            logger.info("Password changed successfully");
        } else {
            logger.error("Current password is incorrect");
        }
    }
}
