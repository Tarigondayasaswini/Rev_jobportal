package org.jobportal.service;

import org.jobportal.model.Company;

public interface CompanyService {


    long createCompany(Company company, long userId);


    Company viewCompany(long companyId);

    Company getCompanyById(long companyId);


    boolean updateCompany(Company company);
}

