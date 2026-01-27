package Testing;

import org.jobportal.config.DBConnection;
import org.jobportal.dao.CompanyDao;
import org.jobportal.dao.CompanyDaoImpl;
import org.jobportal.model.Company;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDaoImplTest {

    private CompanyDao companyDao;
    private final List<Long> testCompanyIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        companyDao = new CompanyDaoImpl();
    }

    @Test
    void testCreateAndFetchCompany() {

        Company company = new Company();
        company.setName("JUnit DAO Company");
        company.setIndustry("IT");
        company.setCompanySize("10-50");
        company.setDescription("DAO Test");
        company.setWebsite("test.com");
        company.setLocation("Hyderabad");

        long companyId = companyDao.createCompany(company);
        assertTrue(companyId > 0);

        testCompanyIds.add(companyId);

        Company fetched = companyDao.findById(companyId);
        assertNotNull(fetched);
        assertEquals("JUnit DAO Company", fetched.getName());
    }

    @Test
    void testUpdateCompany() {

        Company company = new Company();
        company.setName("Before Update");
        company.setIndustry("IT");
        company.setCompanySize("1-10");
        company.setLocation("Chennai");

        long companyId = companyDao.createCompany(company);
        assertTrue(companyId > 0);

        testCompanyIds.add(companyId);

        company.setCompanyId(companyId);
        company.setName("After Update");

        assertTrue(companyDao.updateCompany(company));

        Company updated = companyDao.findById(companyId);
        assertEquals("After Update", updated.getName());
    }

    @AfterEach
    void cleanup() throws Exception {

        try (Connection con = DBConnection.getInstance()) {

            try (PreparedStatement ps =
                         con.prepareStatement(
                                 "UPDATE users SET company_id=NULL WHERE company_id=?")) {
                for (Long id : testCompanyIds) {
                    ps.setLong(1, id);
                    ps.executeUpdate();
                }
            }


            try (PreparedStatement ps =
                         con.prepareStatement(
                                 "DELETE FROM company WHERE company_id=?")) {
                for (Long id : testCompanyIds) {
                    ps.setLong(1, id);
                    ps.executeUpdate();
                }
            }
        }

        testCompanyIds.clear();
    }
}
