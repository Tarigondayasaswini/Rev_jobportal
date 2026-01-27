package org.jobportal.dao;

import org.jobportal.config.DBConnection;
import org.jobportal.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class CompanyDaoImpl implements CompanyDao {

    private static final Logger logger =
            LoggerFactory.getLogger(CompanyDaoImpl.class);

    @Override
    public long createCompany(Company company) {

        String sql = """
            INSERT INTO company
            (name, industry, company_size, description, website, location)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, company.getName());
            ps.setString(2, company.getIndustry());
            ps.setString(3, company.getCompanySize());
            ps.setString(4, company.getDescription());
            ps.setString(5, company.getWebsite());
            ps.setString(6, company.getLocation());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            logger.error("Company creation failed", e);
        }
        return -1;
    }

    @Override
    public Company getCompanyById(long companyId) {

        String sql = "SELECT * FROM company WHERE company_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, companyId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Company company = new Company();
                company.setCompanyId(companyId);
                company.setName(rs.getString("name"));
                company.setIndustry(rs.getString("industry"));
                company.setCompanySize(rs.getString("company_size"));
                company.setDescription(rs.getString("description"));
                company.setWebsite(rs.getString("website"));
                company.setLocation(rs.getString("location"));
                return company;
            }

        } catch (SQLException e) {
            logger.error("Fetch company failed", e);
        }
        return null;
    }

    @Override
    public boolean updateCompany(Company company) {

        String sql = """
            UPDATE company
            SET name=?, industry=?, company_size=?,
                description=?, website=?, location=?
            WHERE company_id=?
        """;

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, company.getName());
            ps.setString(2, company.getIndustry());
            ps.setString(3, company.getCompanySize());
            ps.setString(4, company.getDescription());
            ps.setString(5, company.getWebsite());
            ps.setString(6, company.getLocation());
            ps.setLong(7, company.getCompanyId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Update company failed", e);
            return false;
        }
    }

    @Override
    public Company findById(long companyId) {

        String sql = "SELECT * FROM company WHERE company_id=?";

        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, companyId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Company c = new Company();
                c.setCompanyId(rs.getLong("company_id"));
                c.setName(rs.getString("name"));
                c.setIndustry(rs.getString("industry"));
                c.setCompanySize(rs.getString("company_size"));
                c.setDescription(rs.getString("description"));
                c.setWebsite(rs.getString("website"));
                c.setLocation(rs.getString("location"));
                return c;
            }

        } catch (SQLException e) {
            logger.error("Fetch company failed", e);
        }
        return null;
    }

}

