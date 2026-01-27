package serviceTest;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jobportal.dao.JobApplicationDao;
import org.jobportal.dao.JobDao;
import org.jobportal.model.JobApplication;
import org.jobportal.model.Notification;
import org.jobportal.service.JobApplicationServiceImpl;
import org.jobportal.service.NotificationService;
import org.jobportal.util.ApplyStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

class JobApplicationServiceTest {

    @Mock
    private JobApplicationDao jobApplicationDao;

    @Mock
    private JobDao jobDao;

    @Mock
    private NotificationService notificationService;

    @Spy
    private JobApplicationServiceImpl service;

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);


        Field daoField =
                JobApplicationServiceImpl.class
                        .getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(service, jobApplicationDao);


        Field jobDaoField =
                JobApplicationServiceImpl.class
                        .getDeclaredField("jobDao");
        jobDaoField.setAccessible(true);
        jobDaoField.set(service, jobDao);


        Field notificationField =
                JobApplicationServiceImpl.class
                        .getDeclaredField("notificationService");
        notificationField.setAccessible(true);
        notificationField.set(service, notificationService);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        closeable.close();
    }

    // ---------------- APPLY WITH SELECTED RESUME ----------------

    @Test
    void testApplyWithSelectedResume_Success() {

        when(jobDao.existsById(1L)).thenReturn(true);
        when(jobDao.isJobOpen(1L)).thenReturn(true);
        when(jobApplicationDao.hasAppliedForJob(1L, 2L))
                .thenReturn(false);
        when(jobApplicationDao.save(any(JobApplication.class)))
                .thenReturn(true);

        ApplyStatus status =
                service.applyWithSelectedResume(
                        1L, 2L, 3L, "cover");

        assertEquals(ApplyStatus.SUCCESS, status);
    }

    @Test
    void testApplyWithSelectedResume_JobNotFound() {

        when(jobDao.existsById(1L)).thenReturn(false);

        ApplyStatus status =
                service.applyWithSelectedResume(
                        1L, 2L, 3L, "cover");

        assertEquals(ApplyStatus.JOB_NOT_FOUND, status);
    }

    // ---------------- APPLY WITH LATEST RESUME ----------------

    @Test
    void testApplyWithLatestResume_Success() {

        when(jobDao.existsById(1L)).thenReturn(true);
        when(jobDao.isJobOpen(1L)).thenReturn(true);
        when(jobApplicationDao.findLatestResumeId(2L))
                .thenReturn(5L);
        when(jobApplicationDao.hasAppliedForJob(1L, 2L))
                .thenReturn(false);
        when(jobApplicationDao.save(any(JobApplication.class)))
                .thenReturn(true);

        ApplyStatus status =
                service.applyWithLatestResume(
                        1L, 2L, "cover");

        assertEquals(ApplyStatus.SUCCESS, status);
    }

    @Test
    void testApplyWithLatestResume_ResumeNotFound() {

        when(jobDao.existsById(1L)).thenReturn(true);
        when(jobDao.isJobOpen(1L)).thenReturn(true);
        when(jobApplicationDao.findLatestResumeId(2L))
                .thenReturn(null);

        ApplyStatus status =
                service.applyWithLatestResume(
                        1L, 2L, "cover");

        assertEquals(ApplyStatus.RESUME_NOT_FOUND, status);
    }

    // ---------------- SIMPLE DAO CALLS ----------------

    @Test
    void testGetMyApplications() {

        when(jobApplicationDao.findBySeeker(2L))
                .thenReturn(Collections.emptyList());

        List<JobApplication> list =
                service.getMyApplications(2L);

        assertNotNull(list);
    }

    @Test
    void testWithdrawApplication() {

        when(jobApplicationDao.withdraw(1L, 2L))
                .thenReturn(true);

        boolean result =
                service.withdrawApplication(1L, 2L, "reason");

        assertTrue(result);
    }

    @Test
    void testViewApplicantsByJob() {

        when(jobApplicationDao.findApplicantsByJob(1L))
                .thenReturn(Collections.emptyList());

        List<Object[]> result =
                service.viewApplicantsByJob(1L);

        assertNotNull(result);
    }

    // ---------------- BULK SHORTLIST / REJECT ----------------

    @Test
    void testBulkShortlistOrReject_Success() {

        List<Long> appIds = Arrays.asList(1L, 2L);

        when(jobApplicationDao
                .bulkUpdateStatus(appIds, "SHORTLISTED", "ok"))
                .thenReturn(true);

        when(jobApplicationDao.findSeekerIdByApplication(anyLong()))
                .thenReturn(10L);

        boolean result =
                service.bulkShortlistOrReject(
                        appIds, "SHORTLISTED", "ok");

        assertTrue(result);
        verify(notificationService, times(2))
                .notifyUser(any(Notification.class));
    }

    // ---------------- NOTIFY JOB MATCHES ----------------

    @Test
    void testNotifyJobMatches() {

        when(jobApplicationDao
                .findMatchingSeekers("Java", "HYD"))
                .thenReturn(Arrays.asList(1L, 2L));

        service.notifyJobMatches("Java", "HYD");

        verify(notificationService, times(2))
                .notifyUser(any(Notification.class));
    }

    // ---------------- FILTER ----------------

    @Test
    void testFilterApplicants() {

        when(jobApplicationDao.filterApplicants(
                anyLong(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<Object[]> result =
                service.filterApplicants(
                        1L, 3, "Java", "B.Tech",
                        LocalDate.now());

        assertNotNull(result);
    }
}
