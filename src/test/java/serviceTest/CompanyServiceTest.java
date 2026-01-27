package serviceTest;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.jobportal.dao.CompanyDao;
import org.jobportal.dao.UserDao;
import org.jobportal.model.Company;
import org.jobportal.service.CompanyServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

class CompanyServiceTest {

    @Mock
    private CompanyDao companyDao;

    @Mock
    private UserDao userDao;

    @Spy
    private CompanyServiceImpl companyService;

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);

        // inject dao field
        Field daoField =
                CompanyServiceImpl.class.getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(companyService, companyDao);

        // inject companyDao field
        Field companyDaoField =
                CompanyServiceImpl.class.getDeclaredField("companyDao");
        companyDaoField.setAccessible(true);
        companyDaoField.set(companyService, companyDao);

        // inject userDao field
        Field userDaoField =
                CompanyServiceImpl.class.getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(companyService, userDao);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        closeable.close();
    }

    @Test
    void testCreateCompany_Success() {

        Company company = new Company();

        when(companyDao.createCompany(company))
                .thenReturn(5L);

        long companyId =
                companyService.createCompany(company, 1L);

        assertEquals(5L, companyId);

        verify(companyDao, times(1))
                .createCompany(company);
        verify(userDao, times(1))
                .updateCompanyId(1L, 5L);
    }

    @Test
    void testCreateCompany_Failure() {

        Company company = new Company();

        when(companyDao.createCompany(company))
                .thenReturn(0L);

        long companyId =
                companyService.createCompany(company, 1L);

        assertEquals(0L, companyId);

        verify(userDao, never())
                .updateCompanyId(anyLong(), anyLong());
    }

    @Test
    void testViewCompany() {

        Company company = mock(Company.class);

        when(companyDao.getCompanyById(10L))
                .thenReturn(company);

        Company result =
                companyService.viewCompany(10L);

        assertNotNull(result);
    }

    @Test
    void testUpdateCompany() {

        Company company = mock(Company.class);

        when(companyDao.updateCompany(company))
                .thenReturn(true);

        boolean result =
                companyService.updateCompany(company);

        assertTrue(result);
    }

    @Test
    void testGetCompanyById() {

        Company company = mock(Company.class);

        when(companyDao.findById(7L))
                .thenReturn(company);

        Company result =
                companyService.getCompanyById(7L);

        assertNotNull(result);
    }
}
