package com.heritage.company.services;

import com.heritage.company.exceptions.CompanyAlreadyExistsException;
import com.heritage.company.models.Company;
import com.heritage.company.models.CompanyRequest;
import com.heritage.company.repositories.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyServiceImplTest {
}
