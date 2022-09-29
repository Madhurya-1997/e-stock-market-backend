package com.heritage.company.repositories;

import com.heritage.company.models.Company;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    Company company1;


    @BeforeEach
    void setUp() {
        Company company1 = new Company("1", "H01", "Heritage", "H CEO", 10000000000l, "heritage.com", "NSE");
        companyRepository.save(company1);
    }

    @AfterEach
    void tearDown() {
        companyRepository.deleteAll();
    }

    @Test
    public void testFindByCode() {
        Optional<Company> companyOptional = companyRepository.findByCode("H01");
        assertEquals(companyOptional.get(), company1);
    }

    @Test
    void testDeleteByCode() {
//        companyRepository.deleteByCode("H01");
        Mockito.verify(companyRepository, Mockito.times(1)).deleteByCode("H01");
    }
}