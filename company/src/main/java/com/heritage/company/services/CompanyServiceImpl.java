package com.heritage.company.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.heritage.company.clients.StockClient;
import com.heritage.company.config.CompanyServiceConfig;
import com.heritage.company.config.RestServiceConfig;
import com.heritage.company.exceptions.*;
import com.heritage.company.models.*;
import com.heritage.company.repositories.CompanyRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService{

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

//    @Autowired
//    private StockClient stockClient;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyServiceConfig companyServiceConfig;


    @Value("${stock-uri}")
    private String STOCK_URI;

    @Override
    public List<CompanyDetails> getAllCompanies(String traceID) {
        log.debug("Invoking getAllCompanies service");
        // fetch only the latest stock created for each company
        List<Company> companyList = companyRepository.findAll();
        List<CompanyDetails> details = new ArrayList<>();

        companyList.forEach(company -> {
            ResponseEntity<List<Stock>> stocks =
                    RestServiceConfig.getRestTemplate(traceID).exchange(STOCK_URI + "get/" + company.getCode(),
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Stock>>() {
                            });

            List<Stock> stockList = stocks.getBody();
            Collections.sort(stockList, (stock1, stock2) -> stock2.getCreatedAt().compareTo(stock1.getCreatedAt()));
            details.add(new CompanyDetails(company, stockList));
        });
        return details;
    }

    @Override
    public Company addNewCompany(String traceID, Company company) {
        log.debug("Invoking addNewCompany service");

        Optional<Company> existingCompany = companyRepository.findByCode(company.getCode());
        log.debug("Existing company is: " + existingCompany);

        if (existingCompany.isPresent()) {
            throw new CompanyAlreadyExistsException("Company code already exists");
        }
        return companyRepository.save(company);
    }


    @Override
    public CompanyDetails getCompanyFromCode(String traceID, String code ) {
        log.debug("Invoking getCompanyFromCode service");

        Optional<Company> existingCompany = companyRepository.findByCode(code);
        if (existingCompany.isEmpty()) {
            throw new CompanyNotFoundException();
        }
        // get all stock details for this company
        ResponseEntity<List<Stock>> stocks =
                RestServiceConfig.getRestTemplate(traceID).exchange(STOCK_URI + "get/" + code,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Stock>>() {
                        });
        List<Stock> stockList = stocks.getBody();
        CompanyDetails companyDetails = new CompanyDetails(existingCompany.get(), stockList);

        Collections.sort(stockList, (s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()));


        return companyDetails;
    }

    @Override
    public void deleteCompany(String traceID, String code) {

        log.debug("Invoking deleteCompany service");

        Optional<Company> existingCompany = companyRepository.findByCode(code);
        log.debug("Existing company is: " + existingCompany);

        invokeStockServiceClient(traceID, code);

        if (existingCompany.isEmpty()) {
           throw new CompanyNotFoundException();
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
     * Handling Stock service client failure
     * @param companyCode
     * @return
     */
    @Retry(name = "retryInvokeStockServiceClient", fallbackMethod = "invokeStockServiceClientFallback")
    private void invokeStockServiceClient(String traceID, String companyCode) {
        RestTemplate restTemplate = RestServiceConfig.getRestTemplate(traceID);
        restTemplate.delete(STOCK_URI + "delete/" + companyCode);
    }

    private Company invokeStockServiceClientFallback(String traceID, String code, Throwable t) {
        Company fallbackCompany = new Company();
        fallbackCompany.setFallbackMessage("Stock service is not responding. Please try again later !!");
        return fallbackCompany;
    }
}
