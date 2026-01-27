package org.jobportal.service;

import org.jobportal.model.Notification;
import java.util.List;

public interface NotificationService {

    boolean notifyUser(Notification notification);

    List<Notification> getUserNotifications(long userId);

    boolean markNotificationRead(long notificationId);
}
