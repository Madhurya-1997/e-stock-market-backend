package com.heritage.company.controllers;

import com.heritage.company.models.Company;
import com.heritage.company.models.CompanyRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

public class AuthFallbackController {
    public Company addNewCompanyFallback(String token, CompanyRequest company, Throwable t) {
        Company fallbackCompany = new Company();
        fallbackCompany.setFallbackMessage("Authentication service is not responding. Please try again later !!");
        return fallbackCompany;
    }
}
