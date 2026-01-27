package org.jobportal.controller;

import org.jobportal.app.MainApp;
import org.jobportal.model.Company;
import org.jobportal.service.CompanyService;
import org.jobportal.service.CompanyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class CompanyController {

    private static final Logger logger =
            LoggerFactory.getLogger(CompanyController.class);

    private static final CompanyService service =
            new CompanyServiceImpl();

//    private static final CompanyService companyService =
//            new CompanyServiceImpl();

    /* ---------------- CREATE COMPANY ---------------- */

    public static void createCompany(long userId) {

        Scanner sc = new Scanner(System.in);
        Company company = new Company();

        logger.info("Enter Company Name:");
        company.setName(sc.nextLine());

        logger.info("Enter Industry:");
        company.setIndustry(sc.nextLine());

        logger.info("Enter Company Size:");
        company.setCompanySize(sc.nextLine());

        logger.info("Enter Description:");
        company.setDescription(sc.nextLine());

        logger.info("Enter Website:");
        company.setWebsite(sc.nextLine());

        logger.info("Enter Location:");
        company.setLocation(sc.nextLine());

        Long companyId =
                service.createCompany(company, userId);// keep session in sync


        if (companyId > 0) {
            logger.info("Company created successfully with ID {}", companyId);
            MainApp.setLoggedInUserCompanyId(companyId);
        } else {
            logger.error("Company creation failed");
        }


    }

    /* ---------------- VIEW COMPANY ---------------- */

    public static void viewCompany(Long companyId) {

        if (companyId == null) {
            logger.error("No company linked to this account");
            return;
        }

        Company company = service.getCompanyById(companyId);

        if (company == null) {
            logger.error("Company not found");
            return;
        }

        logger.info("----- COMPANY DETAILS -----");
        logger.info("Name: {}", company.getName());
        logger.info("Industry: {}", company.getIndustry());
        logger.info("Size: {}", company.getCompanySize());
        logger.info("Description: {}", company.getDescription());
        logger.info("Website: {}", company.getWebsite());
        logger.info("Location: {}", company.getLocation());
    }

    /* ---------------- UPDATE COMPANY ---------------- */

    public static void updateCompany(Long companyId) {

        if (companyId == null) {
            logger.error("No company linked to this account");
            return;
        }

        Scanner sc = new Scanner(System.in);
        Company company = new Company();
        company.setCompanyId(companyId);

        logger.info("Enter New Company Name:");
        company.setName(sc.nextLine());

        logger.info("Enter New Industry:");
        company.setIndustry(sc.nextLine());

        logger.info("Enter New Company Size:");
        company.setCompanySize(sc.nextLine());

        logger.info("Enter New Description:");
        company.setDescription(sc.nextLine());

        logger.info("Enter New Website:");
        company.setWebsite(sc.nextLine());

        logger.info("Enter New Location:");
        company.setLocation(sc.nextLine());

        if (service.updateCompany(company)) {
            logger.info("Company updated successfully");
        } else {
            logger.error("Company update failed");
        }
    }
}
