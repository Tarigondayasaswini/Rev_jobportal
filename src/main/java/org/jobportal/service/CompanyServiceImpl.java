package org.jobportal.service;

import org.jobportal.dao.CompanyDao;
import org.jobportal.dao.CompanyDaoImpl;
import org.jobportal.dao.UserDao;
import org.jobportal.dao.UserDaoImpl;
import org.jobportal.model.Company;

public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao dao = new CompanyDaoImpl();
    private final CompanyDao companyDao = new CompanyDaoImpl();
    private final UserDao userDao = new UserDaoImpl();


    @Override
    public long createCompany(Company company, long userId) {

        long companyId = companyDao.createCompany(company);

        if (companyId > 0) {
            userDao.updateCompanyId(userId, companyId); // 🔥 THIS WAS MISSING
        }

        return companyId;
    }


    @Override
    public Company viewCompany(long companyId) {
        return dao.getCompanyById(companyId);
    }

    @Override
    public boolean updateCompany(Company company) {
        return dao.updateCompany(company);
    }

    @Override
    public Company getCompanyById(long companyId) {
        return dao.findById(companyId);
    }
}

