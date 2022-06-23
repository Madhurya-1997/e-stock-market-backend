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
    public List<Company> findAllCompanies() {
        return companyService.getAllCompanies();
    }


    @PostMapping("/")
    public Company addNewCompany(@RequestHeader("Authorization") String token,
                                 @RequestBody @Valid Company company) {
        return companyService.addNewCompany(token, company);
    }

    @GetMapping("/info/{code}")
    public Company findCompany(@PathVariable ( value = "code") String code) {
        return companyService.getCompanyFromCode(code);
    }

    @DeleteMapping("/delete/{code}")
    public void deleteCompany(@RequestHeader("Authorization") String token,
                              @PathVariable ( value = "code") String code) {
        companyService.deleteCompany(token, code);
    }

}
