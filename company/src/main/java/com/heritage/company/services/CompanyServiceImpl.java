package com.heritage.company.services;

import com.heritage.company.clients.AuthClient;
import com.heritage.company.clients.AuthResponse;
import com.heritage.company.exceptions.*;
import com.heritage.company.models.Company;
import com.heritage.company.models.CompanyFallback;
import com.heritage.company.models.CompanyRequest;
import com.heritage.company.repositories.CompanyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService{

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);


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
        log.debug("Existing company is: " + existingCompany + " with trace ID: " + traceID);

        AuthResponse authResponse = invokeAuthServiceClient(traceID, token);
        log.debug("Existing user is: " + authResponse.getUsername() + " with trace ID: " + traceID);

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
        log.debug("Existing company is: " + existingCompany + " with trace ID: " + traceID);

        AuthResponse authResponse = invokeAuthServiceClient(traceID, token);
        log.debug("Existing user is: " + authResponse.getUsername() + " with trace ID: " + traceID);


        if (authResponse == null) {
            throw new UserNotFoundException();
        }
        if (existingCompany.isEmpty()) {
           throw new CompanyNotFoundException();
        }

        if (!authResponse.getUsername().equalsIgnoreCase(existingCompany.get().getUsername())) {
            throw new NotAllowedException();
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
