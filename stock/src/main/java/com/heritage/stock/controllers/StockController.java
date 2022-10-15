package com.heritage.stock.controllers;

import com.heritage.stock.models.Stock;
import com.heritage.stock.models.StockRequest;
import com.heritage.stock.services.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/stock")
public class StockController {

    @Autowired
    private StockService stockService;


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
        return "<h1>Welcome to Stock Microservice !!</h1>";
    }


    @Operation(summary = "Add a new Stock for a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Stock for respective company added successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Company request Schema",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @PostMapping("/add/{companyCode}")
    public Stock addNewStock(@PathVariable("companyCode")String companyCode,
                             @RequestBody @Valid StockRequest stock){
        return stockService.addNewStock(companyCode, stock);
    }


    @Operation(summary = "Fetch a list of Company Stocks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Company Stock list fetched successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Company request Schema",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/get/{companyCode}")
    public List<Stock> getListOfStocksForCompany(Pageable pageable,
                                                 @PathVariable("companyCode")String companyCode){
        return stockService.getListOfStocksForCompany(pageable, companyCode);
    }

    @Operation(summary = "Fetch a list of Company Stocks for mentioned time span")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Company Stock list fetched for mentioned time span successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Company request Schema",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/get/{companyCode}/{startDate}/{endDate}")
    public List<Stock> getListOfStocksForCompanyWithinTimeSpan(Pageable pageable,
                                                               @PathVariable("companyCode")String companyCode,
                                                               @PathVariable("startDate")String startDate,
                                                               @PathVariable("endDate")String endDate) throws ParseException {
        return stockService.getListOfStocksForCompanyWithinTimeSpan(pageable, companyCode, startDate, endDate);
    }

    @Operation(summary = "Fetch a list of Stocks within a time span")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Stock list fetched for within the mentioned time span",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Company request Schema",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/get/{startDate}/{endDate}")
    public List<Stock> getListOfStocksWithinTimeSpan(Pageable pageable,
                                                               @PathVariable("startDate")String startDate,
                                                               @PathVariable("endDate")String endDate) throws ParseException {
        return stockService.getListOfStocksWithinTimeSpan(pageable, startDate, endDate);
    }

    @Operation(summary = "Delete all Stocks for a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Stocks for respective company deleted successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Company request Schema",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @DeleteMapping("/delete/{companyCode}")
    public void deleteAllStocksForCompany(@PathVariable("companyCode") String companyCode) {

        stockService.deleteAllStocksForCompany(companyCode);
    }
}
