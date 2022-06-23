package com.heritage.company.services;

import com.heritage.company.models.Company;
import com.heritage.company.models.CompanyRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CompanyService {
    public List<Company> getAllCompanies();
    public Company addNewCompany(String token, Company company);
    public Company getCompanyFromCode(String code);
    public void deleteCompany(String token, String code);
}
