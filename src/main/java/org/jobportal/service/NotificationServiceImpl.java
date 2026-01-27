package org.jobportal.service;

import org.jobportal.dao.NotificationDao;
import org.jobportal.dao.NotificationDaoImpl;
import org.jobportal.model.Notification;

import java.util.List;

public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao dao =
            new NotificationDaoImpl();

    @Override
    public boolean notifyUser(Notification notification) {
        return dao.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(long userId) {
        return dao.findByUser(userId);
    }

    @Override
    public boolean markNotificationRead(long notificationId) {
        return dao.markAsRead(notificationId);
    }
}
