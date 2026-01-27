package serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.jobportal.dao.ResumeDao;
import org.jobportal.model.Resume;
import org.jobportal.service.ResumeServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

class ResumeServiceTest {

    @Mock
    private ResumeDao resumeDao;

    @Spy
    private ResumeServiceImpl resumeService;

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);

        // inject dao using reflection
        Field daoField =
                ResumeServiceImpl.class
                        .getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(resumeService, resumeDao);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        closeable.close();
    }

    // ---------------- CREATE RESUME ----------------

    @Test
    void testCreateResume() {

        Resume resume = mock(Resume.class);

        when(resumeDao.createResume(resume))
                .thenReturn(true);

        boolean result =
                resumeService.createResume(resume);

        assertTrue(result);
    }

    // ---------------- VIEW RESUME ----------------

    @Test
    void testViewResume() {

        Resume resume = mock(Resume.class);

        when(resumeDao.getResumeBySeekerId(1L))
                .thenReturn(resume);

        Resume result =
                resumeService.viewResume(1L);

        assertNotNull(result);
    }

    // ---------------- UPDATE RESUME ----------------

    @Test
    void testUpdateResume() {

        Resume resume = mock(Resume.class);

        when(resumeDao.updateResume(resume))
                .thenReturn(true);

        boolean result =
                resumeService.updateResume(resume);

        assertTrue(result);
    }

    // ---------------- UPDATE PROFILE ----------------

    @Test
    void testUpdateProfile() {

        when(resumeDao.updateResumeProfile(
                1L, "B.Tech", "3", "Java", "AWS"))
                .thenReturn(true);

        boolean result =
                resumeService.updateProfile(
                        1L, "B.Tech", "3", "Java", "AWS");

        assertTrue(result);
    }

    // ---------------- VIEW PROFILE ----------------

    @Test
    void testViewProfile() {

        Resume resume = mock(Resume.class);

        when(resumeDao.findBySeekerId(1L))
                .thenReturn(resume);

        Resume result =
                resumeService.viewProfile(1L);

        assertNotNull(result);
    }
}

