package serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.jobportal.dao.JobSeekerDao;
import org.jobportal.dao.ResumeDao;
import org.jobportal.model.JobSeekerProfile;
import org.jobportal.service.JobSeekerServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

class JobSeekerServiceTest {

    @Mock
    private JobSeekerDao jobSeekerDao;

    @Mock
    private ResumeDao resumeDao;

    @Spy
    private JobSeekerServiceImpl jobSeekerService;

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);


        Field daoField =
                JobSeekerServiceImpl.class
                        .getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(jobSeekerService, jobSeekerDao);


        Field jobSeekerDaoField =
                JobSeekerServiceImpl.class
                        .getDeclaredField("jobSeekerDao");
        jobSeekerDaoField.setAccessible(true);
        jobSeekerDaoField.set(jobSeekerService, jobSeekerDao);


        Field resumeDaoField =
                JobSeekerServiceImpl.class
                        .getDeclaredField("resumeDao");
        resumeDaoField.setAccessible(true);
        resumeDaoField.set(jobSeekerService, resumeDao);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        closeable.close();
    }

    // ---------------- CREATE PROFILE ----------------

    @Test
    void testCreateProfile() {

        JobSeekerProfile profile = mock(JobSeekerProfile.class);

        when(jobSeekerDao.createProfile(profile))
                .thenReturn(true);

        boolean result =
                jobSeekerService.createProfile(profile);

        assertTrue(result);
    }

    // ---------------- VIEW PROFILE ----------------

    @Test
    void testViewProfile() {

        JobSeekerProfile profile = mock(JobSeekerProfile.class);

        when(jobSeekerDao.getProfileBySeekerId(1L))
                .thenReturn(profile);

        JobSeekerProfile result =
                jobSeekerService.viewProfile(1L);

        assertNotNull(result);
    }

    // ---------------- UPDATE PROFILE ----------------

    @Test
    void testUpdateProfile() {

        JobSeekerProfile profile = mock(JobSeekerProfile.class);

        when(jobSeekerDao.updateProfile(profile))
                .thenReturn(true);

        boolean result =
                jobSeekerService.updateProfile(profile);

        assertTrue(result);
    }

    // ---------------- PROFILE COMPLETION STATUS ----------------

    @Test
    void testUpdateProfileCompletionStatus_Completed() {

        when(jobSeekerDao.profileExists(1L))
                .thenReturn(true);

        when(resumeDao.resumeExistsForSeeker(1L))
                .thenReturn(true);

        jobSeekerService.updateProfileCompletionStatus(1L);

        verify(jobSeekerDao, times(1))
                .updateProfileCompletionStatus(1L, true);
    }

    @Test
    void testUpdateProfileCompletionStatus_NotCompleted() {

        when(jobSeekerDao.profileExists(1L))
                .thenReturn(true);

        when(resumeDao.resumeExistsForSeeker(1L))
                .thenReturn(false);

        jobSeekerService.updateProfileCompletionStatus(1L);

        verify(jobSeekerDao, times(1))
                .updateProfileCompletionStatus(1L, false);
    }
}

