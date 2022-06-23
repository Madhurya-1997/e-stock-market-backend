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
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }


//    @Retry(name = "retryAddNewCompanyFallback", fallbackMethod = "addNewCompanyFallback")
//    @CircuitBreaker(name = "addNewCompanyCircuitBreaker", fallbackMethod = "addNewCompanyFallback")
    @Override
    public Company addNewCompany(String token, Company company) {

        log.debug("Invoking addNewCompany service...");

        Optional<Company> existingCompany = companyRepository.findByCode(company.getCode());
        AuthResponse authResponse = invokeAuthServiceClient(token);
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

        company.setUsername(authResponse.getUsername());
        return companyRepository.save(company);
    }


    @Override
    public Company getCompanyFromCode(String code) {
        Optional<Company> existingCompany = companyRepository.findByCode(code);

        if (existingCompany.isEmpty()) {
            throw new CompanyNotFoundException();
        }
        return existingCompany.get();
    }

    @Override
    public void deleteCompany(String token, String code) {

        log.debug("Invoking deleteCompany service...");

        Optional<Company> existingCompany = companyRepository.findByCode(code);
        AuthResponse authResponse = null;

        try {
            authResponse = authClient.verifyUser(token);
        } catch(Exception e) {
            throw new UserTokenExpiredException();
        }
        if (authResponse == null) {
            throw new UserNotFoundException();
        }
        if (existingCompany.isEmpty()) {
           throw new CompanyNotFoundException();
        }
        companyRepository.deleteByCode(code);
    }


    @Retry(name = "retryAddNewCompanyFallback", fallbackMethod = "addNewCompanyFallback")
    private AuthResponse invokeAuthServiceClient(String token) {
       return authClient.verifyUser(token);
    }

    private Company addNewCompanyFallback(String token, CompanyRequest company, Throwable t) {
        Company fallbackCompany = new Company();
        fallbackCompany.setFallbackMessage("Authentication service is not responding or user token has expired. Please try again later !!");
        return fallbackCompany;
    }

}
