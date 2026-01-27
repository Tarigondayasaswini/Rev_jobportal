package serviceTest;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.jobportal.dao.UserDao;
import org.jobportal.model.User;
import org.jobportal.service.UserServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private UserDao userDao;        // MOCK DAO

    @Spy
    private UserServiceImpl userService; // SPY SERVICE (REAL METHODS RUN)

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);

        // Reflection injection: replace private final userDao
        Field field =
                UserServiceImpl.class.getDeclaredField("userDao");
        field.setAccessible(true);
        field.set(userService, userDao);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        closeable.close();
    }

    @Test
    void testRegisterUser() {

        User user = new User();
        user.setEmail("rama@gmail.com");
        user.setPassword("1234");

        when(userDao.registerUser(any(User.class)))
                .thenReturn(true);

        boolean result = userService.register(user);

        assertTrue(result);
        verify(userDao, times(1))
                .registerUser(any(User.class));
    }

    @Test
    void testLoginSuccess() {

        String email = "rama@gmail.com";
        String password = "1234";

        User mockUser = new User();
        mockUser.setEmail(email);

        when(userDao.login(eq(email), anyString()))
                .thenReturn(mockUser);

        User user = userService.login(email, password);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }

    @Test
    void testChangePassword_WrongCurrentPassword() {

        when(userDao.verifyCurrentPassword(1L, "old123"))
                .thenReturn(false);

        boolean result =
                userService.changePassword(1L, "old123", "new123");

        assertFalse(result);
        verify(userDao, never())
                .updatePasswordByUserId(anyLong(), anyString());
    }

    @Test
    void testChangePassword_Success() {

        when(userDao.verifyCurrentPassword(1L, "old123"))
                .thenReturn(true);

        when(userDao.updatePasswordByUserId(1L, "new123"))
                .thenReturn(true);

        boolean result =
                userService.changePassword(1L, "old123", "new123");

        assertTrue(result);
        verify(userDao, times(1))
                .updatePasswordByUserId(1L, "new123");
    }

    @Test
    void testFindByEmail() {

        User user = new User();
        user.setEmail("rama@gmail.com");

        when(userDao.findByEmail("rama@gmail.com"))
                .thenReturn(user);

        User result =
                userService.findByEmail("rama@gmail.com");

        assertNotNull(result);
        assertEquals("rama@gmail.com", result.getEmail());
    }

    @Test
    void testLinkCompany() {

        when(userDao.updateCompanyId(1L, 100L))
                .thenReturn(true);

        boolean result =
                userService.linkCompany(1L, 100L);

        assertTrue(result);
    }
}

