package com.heritage.company.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heritage.company.models.Company;
import com.heritage.company.models.CompanyDetails;

import java.util.List;

public interface CompanyService {
    public List<CompanyDetails> getAllCompanies(String traceID);
    public Company addNewCompany(String traceID, Company company);
    public CompanyDetails getCompanyFromCode(String traceID, String code);
    public void deleteCompany(String traceID, String code);
    public String getPropertyDetails() throws JsonProcessingException;
}
