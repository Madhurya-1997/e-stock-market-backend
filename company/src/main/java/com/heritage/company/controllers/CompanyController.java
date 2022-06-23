package com.heritage.company.controllers;

import com.heritage.company.models.CompanyRequest;
import com.heritage.company.repositories.CompanyRepository;
import com.heritage.company.models.Company;
import com.heritage.company.services.CompanyService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/")
    public String welcome() {
        return "<h1>Welcome to Company Microservice !!</h1>";
    }


    @GetMapping("/getall")
    public List<Company> findAllCompanies(@RequestHeader("e-stock-market-trace-id") String traceID) {
        return companyService.getAllCompanies(traceID);
    }


    @PostMapping("/")
    public Company addNewCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                                 @RequestHeader("Authorization") String token,
                                 @RequestBody @Valid CompanyRequest company) {
        return companyService.addNewCompany(traceID, token, company);
    }

    @GetMapping("/info/{code}")
    public Company findCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                               @PathVariable ( value = "code") String code) {
        return companyService.getCompanyFromCode(traceID, code);
    }

    @DeleteMapping("/delete/{code}")
    public void deleteCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                              @RequestHeader("Authorization") String token,
                              @PathVariable ( value = "code") String code) {
        companyService.deleteCompany(traceID, token, code);
    }

}
