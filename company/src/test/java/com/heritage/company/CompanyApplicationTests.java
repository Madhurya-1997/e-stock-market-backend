package com.heritage.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CompanyApplicationTests {

	@Test
	void contextLoads() {
		CompanyApplication.main(new String[]{});
	}

}
