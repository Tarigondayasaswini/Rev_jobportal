package org.jobportal.dao;

import org.jobportal.model.Company;

public interface CompanyDao {

    long createCompany(Company company);

    Company getCompanyById(long companyId);

    boolean updateCompany(Company company);

    Company findById(long companyId);

}
