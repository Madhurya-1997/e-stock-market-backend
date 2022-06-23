package com.heritage.company.services;

import com.heritage.company.clients.AuthClient;
import com.heritage.company.clients.AuthResponse;
import com.heritage.company.exceptions.CompanyAlreadyExistsException;
import com.heritage.company.exceptions.CompanyNotFoundException;
import com.heritage.company.exceptions.UserNotFoundException;
import com.heritage.company.exceptions.UserTokenExpiredException;
import com.heritage.company.models.Company;
import com.heritage.company.models.CompanyFallback;
import com.heritage.company.models.CompanyRequest;
import com.heritage.company.repositories.CompanyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private AuthClient authClient;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompanies(String traceID) {
        log.debug("Invoking getAllCompanies service with trace ID: " + traceID);
        return companyRepository.findAll();
    }


//    @Retry(name = "retryAddNewCompanyFallback", fallbackMethod = "addNewCompanyFallback")
//    @CircuitBreaker(name = "addNewCompanyCircuitBreaker", fallbackMethod = "addNewCompanyFallback")
    @Override
    public Company addNewCompany(String traceID, String token, CompanyRequest company) {

        log.debug("Invoking addNewCompany service with trace ID: " + traceID);

        Optional<Company> existingCompany = companyRepository.findByCode(company.getCode());
        AuthResponse authResponse = invokeAuthServiceClient(traceID, token);
//        AuthResponse authResponse = null;
//
//        try {
//            authResponse = authClient.verifyUser(token);
//        } catch(Exception e) {
//            throw new UserTokenExpiredException();
//        }
        if (authResponse == null) {
            throw new UserNotFoundException();
        }
        if (existingCompany.isPresent()) {
            throw new CompanyAlreadyExistsException();
        }

        Company newCompany = new Company();
        newCompany.setUsername(authResponse.getUsername());
        newCompany.setCode(company.getCode());
        newCompany.setWebsite(company.getWebsite());
        newCompany.setCeo(company.getCeo());
        newCompany.setTurnover(company.getTurnover());
        newCompany.setName(company.getName());
        newCompany.setStockExchangeEnlisted(company.getStockExchangeEnlisted());

        return companyRepository.save(newCompany);
    }


    @Override
    public Company getCompanyFromCode(String traceID, String code) {
        log.debug("Invoking getCompanyFromCode service with trace ID: " + traceID);

        Optional<Company> existingCompany = companyRepository.findByCode(code);

        if (existingCompany.isEmpty()) {
            throw new CompanyNotFoundException();
        }
        return existingCompany.get();
    }

    @Override
    public void deleteCompany(String traceID, String token, String code) {

        log.debug("Invoking deleteCompany service with trace ID: " + traceID);

        Optional<Company> existingCompany = companyRepository.findByCode(code);
        AuthResponse authResponse = invokeAuthServiceClient(traceID, token);

        if (authResponse == null) {
            throw new UserNotFoundException();
        }
        if (existingCompany.isEmpty()) {
           throw new CompanyNotFoundException();
        }
        companyRepository.deleteByCode(code);
    }


    @Retry(name = "retryAddNewCompanyFallback", fallbackMethod = "addNewCompanyFallback")
    private AuthResponse invokeAuthServiceClient(String traceID, String token) {
       return authClient.verifyUser(traceID, token);
    }

    private Company addNewCompanyFallback(String token, CompanyRequest company, Throwable t) {
        Company fallbackCompany = new Company();
        fallbackCompany.setFallbackMessage("Authentication service is not responding or user token has expired. Please try again later !!");
        return fallbackCompany;
    }

}
