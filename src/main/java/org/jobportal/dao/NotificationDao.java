package org.jobportal.dao;

import org.jobportal.model.Notification;
import java.util.List;

public interface NotificationDao {

    boolean save(Notification notification);

    List<Notification> findByUser(long userId);

    boolean markAsRead(long notificationId);
}
