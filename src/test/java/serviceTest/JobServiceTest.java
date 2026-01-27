package serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.jobportal.dao.JobDao;
import org.jobportal.model.Job;
import org.jobportal.service.JobServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

class JobServiceTest {

    @Mock
    private JobDao jobDao;

    @Spy
    private JobServiceImpl jobService;

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);


        Field daoField =
                JobServiceImpl.class.getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(jobService, jobDao);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        closeable.close();
    }

    // ---------------- POST JOB ----------------

    @Test
    void testPostJob() {

        Job job = mock(Job.class);

        when(jobDao.createJob(job))
                .thenReturn(10L);

        long jobId =
                jobService.postJob(job);

        assertEquals(10L, jobId);
    }

    // ---------------- VIEW COMPANY JOBS ----------------

    @Test
    void testViewCompanyJobs() {

        when(jobDao.getJobsByCompany(1L))
                .thenReturn(Collections.emptyList());

        List<Job> jobs =
                jobService.viewCompanyJobs(1L);

        assertNotNull(jobs);
    }

    // ---------------- VIEW OPEN JOBS ----------------

    @Test
    void testViewOpenJobs() {

        when(jobDao.getOpenJobs())
                .thenReturn(Collections.emptyList());

        List<Job> jobs =
                jobService.viewOpenJobs();

        assertNotNull(jobs);
    }

    // ---------------- CLOSE JOB ----------------

    @Test
    void testCloseJob() {

        when(jobDao.closeJob(5L))
                .thenReturn(true);

        boolean result =
                jobService.closeJob(5L);

        assertTrue(result);
    }

    // ---------------- UPDATE JOB ----------------

    @Test
    void testUpdateJob() {

        Job job = mock(Job.class);

        when(jobDao.updateJob(job))
                .thenReturn(true);

        boolean result =
                jobService.updateJob(job);

        assertTrue(result);
    }

    // ---------------- REOPEN JOB ----------------

    @Test
    void testReopenJob() {

        when(jobDao.reopenJob(6L))
                .thenReturn(true);

        boolean result =
                jobService.reopenJob(6L);

        assertTrue(result);
    }

    // ---------------- SEARCH JOBS ----------------

    @Test
    void testSearchJobsWithFilters() {

        when(jobDao.searchJobsWithFilters(
                any(), any(), any(),
                any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<Job> result =
                jobService.searchJobsWithFilters(
                        "Java", "HYD", 3,
                        "ABC", 5.0, 10.0, "FULL_TIME");

        assertNotNull(result);
    }

    // ---------------- DELETE JOB ----------------

    @Test
    void testDeleteJob() {

        when(jobDao.deleteJob(9L))
                .thenReturn(true);

        boolean result =
                jobService.deleteJob(9L);

        assertTrue(result);
    }

    // ---------------- JOB STATISTICS ----------------

    @Test
    void testGetJobStatistics() {

        Object[] stats = new Object[] { 10, 5 };

        when(jobDao.getJobStatistics(3L))
                .thenReturn(stats);

        Object[] result =
                jobService.getJobStatistics(3L);

        assertNotNull(result);
        assertEquals(2, result.length);
    }
}

