package com.heritage.company.services;

import com.heritage.company.clients.AuthClient;
import com.heritage.company.clients.AuthResponse;
import com.heritage.company.exceptions.CompanyAlreadyExistsException;
import com.heritage.company.exceptions.UserNotFoundException;
import com.heritage.company.models.Company;
import com.heritage.company.models.CompanyRequest;
import com.heritage.company.repositories.CompanyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CompanyServiceImplTest {

    @InjectMocks
    CompanyServiceImpl companyService;

    @Mock
    AuthClient authClient;

    @Mock
    CompanyRepository companyRepository;


    CompanyRequest newCompanyRequest;

    @BeforeEach
    public void setUp() {

        newCompanyRequest = new CompanyRequest("K90", "Kratos", "K CEO", 20000000002l, "kratos.com", "NSE");

    }

    @AfterEach
    void tearDown() {
        companyRepository.deleteAll();
    }

    @Test
    public void testGetAllCompanies() {
        Company company1 = new Company("1", "H01", "Heritage", "H CEO", 10000000000l, "heritage.com", "NSE", "H-username");
        Company company2 = new Company("2", "N11", "Naruto", "N CEO", 10000000002l, "naruto.com", "NSE", "N-username");
        List<Company> companies = List.of(company1, company2);
        companyRepository.save(company1);
        companyRepository.save(company2);

        when(companyRepository.findAll()).thenReturn(companies);
        assertEquals(companyService.getAllCompanies("123"), companyRepository.findAll());
    }

    @Test
    public void testAddNewCompanySuccess() {
        AuthResponse authResponseSuccess = new AuthResponse("123123123", "H-username", "husername@gmail.com");
        when(authClient.verifyUser("123", "token")).thenReturn(authResponseSuccess);
        Company newCompany = new Company();
        newCompany.setUsername(authResponseSuccess.getUsername());
        newCompany.setCode(newCompanyRequest.getCode());
        newCompany.setWebsite(newCompanyRequest.getWebsite());
        newCompany.setCeo(newCompanyRequest.getCeo());
        newCompany.setTurnover(newCompanyRequest.getTurnover());
        newCompany.setName(newCompanyRequest.getName());
        newCompany.setStockExchangeEnlisted(newCompanyRequest.getStockExchangeEnlisted());
        when(companyRepository.save(newCompany)).thenReturn(companyRepository.findByCode("K90").get());
        assertEquals(companyService.addNewCompany("123", "token", new CompanyRequest("K90", "Kratos", "K CEO", 20000000002l, "kratos.com", "NSE")), newCompany);
    }

    @Test
    public void testAddNewCompanyFailure1() {
        AuthResponse authResponseFailure = new AuthResponse("failedjwt123", "H-username", "husername@gmail.com");
        when(authClient.verifyUser("123", "token")).thenReturn(authResponseFailure);
        assertThrows(UserNotFoundException.class,() -> companyService.addNewCompany("123", "token", newCompanyRequest));
    }
    @Test
    public void testAddNewCompanyFailure2() {
        AuthResponse response = new AuthResponse("123123123", "H-username", "husername@gmail.com");
        when(authClient.verifyUser("123", "token")).thenReturn(response);
        when(companyRepository.findByCode("K90").get()).thenReturn(new Company("1,", "K90", "Kratos", "K CEO", 20000000002l, "kratos.com", "NSE", "H-username"));
        assertThrows(CompanyAlreadyExistsException.class,() -> companyService.addNewCompany("123", "token", newCompanyRequest));
    }
}
