package com.heritage.company.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heritage.company.config.CompanyServiceConfig;
import com.heritage.company.models.CompanyDetails;
import com.heritage.company.models.CompanyRequest;
import com.heritage.company.models.Company;
import com.heritage.company.services.CompanyService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyServiceConfig companyServiceConfig;


    @Operation(summary = "Display home page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Home page is working",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/")
    public String welcome() {
        return "<h1>Welcome to Company Microservice !!</h1>";
    }



    @Operation(summary = "Fetch all Company details from Company DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched all Company details from Company DB",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/getall")
    @Timed(value = "findAllCompanies.time", description = "Time taken to return all company details")
    public List<CompanyDetails> findAllCompanies(@RequestHeader("e-stock-market-trace-id") String traceID) {
        return companyService.getAllCompanies(traceID);
    }


    @Operation(summary = "Register a new Company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Registration of Company details done",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Company request Schema",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Unauthorized to add new stock",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized to register new company",
                    content = @Content)
    })
    @PostMapping("/")
    @Timed(value = "addNewCompany.time", description = "Time taken to register a new company")
    public Company addNewCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                                 @RequestHeader("Authorization") String token,
                                 @RequestBody @Valid CompanyRequest company) {
        return companyService.addNewCompany(traceID, token, company);
    }

    @Operation(summary = "Find a Company with all its stocks based on Company code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the Company details",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/info/{code}")
    @Timed(value = "findCompany.time", description = "Time taken to find company based on company code")
    public CompanyDetails findCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                                      @PathVariable ( value = "code") String code) {
        return companyService.getCompanyFromCode(traceID, code);
    }


    @Operation(summary = "Delete Company details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Removed Company details",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Company request Schema",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Unauthorized to add new stock",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized to delete other companies",
                    content = @Content)
    })
    @DeleteMapping("/delete/{code}")
    @Timed(value = "deleteCompany.time", description = "Time taken to delete company based on company code")
    public void deleteCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                              @RequestHeader("Authorization") String token,
                              @PathVariable ( value = "code") String code) {
        companyService.deleteCompany(traceID, token, code);
    }

    @GetMapping("/properties")
    public String getPropertyDetails() throws JsonProcessingException{
        return companyService.getPropertyDetails();
    }

}
