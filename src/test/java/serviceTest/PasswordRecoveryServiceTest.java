package serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.jobportal.dao.PasswordRecoveryDao;
import org.jobportal.model.PasswordRecovery;
import org.jobportal.service.PasswordRecoveryServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

class PasswordRecoveryServiceTest {

    @Mock
    private PasswordRecoveryDao passwordRecoveryDao;

    @Spy
    private PasswordRecoveryServiceImpl passwordRecoveryService;

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);


        Field daoField =
                PasswordRecoveryServiceImpl.class
                        .getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(passwordRecoveryService, passwordRecoveryDao);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        closeable.close();
    }

    // ---------------- SAVE RECOVERY DETAILS ----------------

    @Test
    void testSaveRecoveryDetails() {

        PasswordRecovery recovery = mock(PasswordRecovery.class);

        when(passwordRecoveryDao.save(recovery))
                .thenReturn(true);

        boolean result =
                passwordRecoveryService.saveRecoveryDetails(recovery);

        assertTrue(result);
        verify(passwordRecoveryDao, times(1))
                .save(recovery);
    }

    // ---------------- GET RECOVERY DETAILS ----------------

    @Test
    void testGetRecoveryDetails() {

        PasswordRecovery recovery = mock(PasswordRecovery.class);

        when(passwordRecoveryDao.findByUserId(1L))
                .thenReturn(recovery);

        PasswordRecovery result =
                passwordRecoveryService.getRecoveryDetails(1L);

        assertNotNull(result);
    }
}
