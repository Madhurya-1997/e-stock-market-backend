package com.heritage.company.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.heritage.company.clients.AuthClient;
import com.heritage.company.clients.AuthResponse;
import com.heritage.company.clients.StockClient;
import com.heritage.company.config.CompanyServiceConfig;
import com.heritage.company.exceptions.*;
import com.heritage.company.models.*;
import com.heritage.company.repositories.CompanyRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService{

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);


    @Autowired
    private AuthClient authClient;

    @Autowired
    private StockClient stockClient;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyServiceConfig companyServiceConfig;

    @Override
    public List<CompanyDetails> getAllCompanies(String traceID) {
        log.debug("Invoking getAllCompanies service with trace ID: " + traceID);


        // fetch only the latest stock created for each company
        List<Company> companyList = companyRepository.findAll();
        List<CompanyDetails> details = new ArrayList<>();
        companyList.forEach(company -> {
            List<Stock> stockList = stockClient.getListOfStocksForCompany(traceID, company.getCode());
            Collections.sort(stockList, Comparator.comparing(Stock::getCreatedAt));
            details.add(new CompanyDetails(company, stockList));
        });
        return details;
    }

    @Override
    public Company addNewCompany(String traceID, String token, CompanyRequest company) {

        log.debug("Invoking addNewCompany service with trace ID: " + traceID);

        Optional<Company> existingCompany = companyRepository.findByCode(company.getCode());
        log.debug("Existing company is: " + existingCompany + " with trace ID: " + traceID);

        AuthResponse authResponse = invokeAuthServiceClient(traceID, token);
        log.debug("Existing user is: " + authResponse.getUsername() + " with trace ID: " + traceID);

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
    public CompanyDetails getCompanyFromCode(String traceID, String code ) {
        log.debug("Invoking getCompanyFromCode service with trace ID: " + traceID);

        Optional<Company> existingCompany = companyRepository.findByCode(code);

        // get all stock details for this company
        List<Stock> stockList = stockClient.getListOfStocksForCompany(traceID, code);
        CompanyDetails companyDetails = new CompanyDetails(existingCompany.get(), stockList);


        if (existingCompany.isEmpty()) {
            throw new CompanyNotFoundException();
        }
        return companyDetails;
    }

    @Override
    public void deleteCompany(String traceID, String token, String code) {

        log.debug("Invoking deleteCompany service with trace ID: " + traceID);

        Optional<Company> existingCompany = companyRepository.findByCode(code);
        log.debug("Existing company is: " + existingCompany + " with trace ID: " + traceID);

        AuthResponse authResponse = invokeAuthServiceClient(traceID, token);
        log.debug("Existing user is: " + authResponse.getUsername() + " with trace ID: " + traceID);

        String deletedStocks = invokeStockServiceClient(traceID, token, code);
        log.debug("Message from stock client after deletion: " + deletedStocks);


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

    @Override
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        CompanyProperties properties = new CompanyProperties(companyServiceConfig.getMessage(),
                companyServiceConfig.getBuildVersion(),
                companyServiceConfig.getMailDetails(),
                companyServiceConfig.getStockExchange());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }


    /**
     * Handling Auth client failure
     * @param traceID
     * @param token
     * @return
     */
    @Retry(name = "retryInvokeAuthServiceClient", fallbackMethod = "invokeAuthServiceClientFallback")
    private AuthResponse invokeAuthServiceClient(String traceID, String token) {
        return authClient.verifyUser(traceID, token);
    }

    private Company invokeAuthServiceClientFallback(String traceID, String token, CompanyRequest company, Throwable t) {
        Company fallbackCompany = new Company();
        fallbackCompany.setFallbackMessage("Authentication service is not responding or user token has expired. Please try again later !! Trace ID: " + traceID);
        return fallbackCompany;
    }


    /**
     * Handling Stock service client failure
     * @param traceID
     * @param token
     * @param companyCode
     * @return
     */
    @Retry(name = "retryInvokeStockServiceClient", fallbackMethod = "invokeStockServiceClientFallback")
    private String invokeStockServiceClient(String traceID, String token, String companyCode) {
        return stockClient.deleteAllStocksForCompany(traceID, token, companyCode);
    }

    private Company invokeStockServiceClientFallback(String traceID, String token, String code, Throwable t) {
        Company fallbackCompany = new Company();
        fallbackCompany.setFallbackMessage("Stock service is not responding. Please try again later !!");
        return fallbackCompany;
    }
}
