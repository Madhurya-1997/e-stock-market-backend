package com.heritage.company.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heritage.company.models.Company;
import com.heritage.company.models.CompanyDetails;
import com.heritage.company.models.CompanyRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {
    public List<Company> getAllCompanies(String traceID);
    public Company addNewCompany(String traceID, String token, CompanyRequest company);
    public CompanyDetails getCompanyFromCode(String traceID, String code);
    public void deleteCompany(String traceID, String token, String code);
    public String getPropertyDetails() throws JsonProcessingException;
}
