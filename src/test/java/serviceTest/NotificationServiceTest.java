package serviceTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.jobportal.dao.NotificationDao;
import org.jobportal.model.Notification;
import org.jobportal.service.NotificationServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

class NotificationServiceTest {

    @Mock
    private NotificationDao notificationDao;

    @Spy
    private NotificationServiceImpl notificationService;

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);


        Field daoField =
                NotificationServiceImpl.class
                        .getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(notificationService, notificationDao);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        closeable.close();
    }

    // ---------------- NOTIFY USER ----------------

    @Test
    void testNotifyUser() {

        Notification notification = mock(Notification.class);

        when(notificationDao.save(notification))
                .thenReturn(true);

        boolean result =
                notificationService.notifyUser(notification);

        assertTrue(result);
        verify(notificationDao, times(1))
                .save(notification);
    }

    // ---------------- GET USER NOTIFICATIONS ----------------

    @Test
    void testGetUserNotifications() {

        when(notificationDao.findByUser(1L))
                .thenReturn(Collections.emptyList());

        List<Notification> notifications =
                notificationService.getUserNotifications(1L);

        assertNotNull(notifications);
    }

    // ---------------- MARK NOTIFICATION READ ----------------

    @Test
    void testMarkNotificationRead() {

        when(notificationDao.markAsRead(5L))
                .thenReturn(true);

        boolean result =
                notificationService.markNotificationRead(5L);

        assertTrue(result);
    }
}

