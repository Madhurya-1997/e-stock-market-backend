package com.heritage.company.services;

import com.heritage.company.exceptions.CompanyAlreadyExistsException;
import com.heritage.company.models.Company;
import com.heritage.company.repositories.CompanyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

@SpringBootTest
public class CompanyServiceImplTest {

    @InjectMocks
    CompanyServiceImpl companyService;

    @Mock
    CompanyRepository companyRepository;


    Company newCompanyRequest;

    @BeforeEach
    public void setUp() {

        newCompanyRequest = new Company("123", "K90", "Kratos", "K CEO", 20000000002l, "kratos.com", "NSE");

    }

    @AfterEach
    void tearDown() {
        companyRepository.deleteAll();
    }

    @Test
    public void testGetAllCompanies() {
        Company company1 = new Company("1", "H01", "Heritage", "H CEO", 10000000000l, "heritage.com", "NSE");
        Company company2 = new Company("2", "N11", "Naruto", "N CEO", 10000000002l, "naruto.com", "NSE");
        List<Company> companies = List.of(company1, company2);
        companyRepository.save(company1);
        companyRepository.save(company2);

        when(companyRepository.findAll()).thenReturn(companies);
        assertEquals(companyService.getAllCompanies("123"), companyRepository.findAll());
    }

    @Test
    public void testAddNewCompanySuccess() {
        Company newCompany = new Company();
        newCompany.setCode(newCompanyRequest.getCode());
        newCompany.setWebsite(newCompanyRequest.getWebsite());
        newCompany.setCeo(newCompanyRequest.getCeo());
        newCompany.setTurnover(newCompanyRequest.getTurnover());
        newCompany.setName(newCompanyRequest.getName());
        newCompany.setStockExchangeEnlisted(newCompanyRequest.getStockExchangeEnlisted());
        when(companyRepository.save(newCompany)).thenReturn(companyRepository.findByCode("K90").get());
        assertEquals(companyService.addNewCompany("123", new Company("123", "K90", "Kratos", "K CEO", 20000000002l, "kratos.com", "NSE")), newCompany);
    }

    @Test
    public void testAddNewCompanyFailure2() {
        when(companyRepository.findByCode("K90").get()).thenReturn(new Company("123", "K90", "Kratos", "K CEO", 20000000002l, "kratos.com", "NSE"));
        assertThrows(CompanyAlreadyExistsException.class,() -> companyService.addNewCompany("123", newCompanyRequest));
    }
}
