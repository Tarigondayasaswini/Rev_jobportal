package org.jobportal.controller;
import org.jobportal.model.User;
import org.jobportal.service.UserService;
import org.jobportal.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class LoginController {

    private static final Logger logger =
            LoggerFactory.getLogger(org.jobportal.controller.LoginController.class);

    public static User login() {

        Scanner sc = new Scanner(System.in);
        UserService service = new UserServiceImpl();

        logger.info("Enter Email:");
        String email = sc.nextLine();

        logger.info("Enter Password:");
        String password = sc.nextLine();

        User user = service.login(email, password);

        if (user == null) {
            logger.error("Invalid credentials");
        } else {
            logger.info("Login successful. Role: {}", user.getRole());
        }

        return user;
    }
}

