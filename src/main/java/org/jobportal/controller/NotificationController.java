package org.jobportal.controller;

import org.jobportal.model.Notification;
import org.jobportal.service.NotificationService;
import org.jobportal.service.NotificationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;



public class NotificationController {

    private static final Logger logger =
            LoggerFactory.getLogger(NotificationController.class);

    private static final NotificationService service =
            new NotificationServiceImpl();



    public static void viewMyNotifications(long userId) {

        List<Notification> notifications =
                service.getUserNotifications(userId);

        if (notifications.isEmpty()) {
            logger.info("No notifications available");
            return;
        }

        logger.info("=== MY NOTIFICATIONS ===");

        for (Notification n : notifications) {
            logger.info(
                    "[{}] {} | {}",
                    n.isRead() ? "READ" : "NEW",
                    n.getMessage(),
                    n.getCreatedAt()
            );

            if (!n.isRead()) {
                service.markNotificationRead(n.getNotificationId());
            }
        }

    }


}
