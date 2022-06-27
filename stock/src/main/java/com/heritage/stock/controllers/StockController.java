package com.heritage.stock.controllers;

import com.heritage.stock.models.Stock;
import com.heritage.stock.services.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized to add new stock",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Unauthorized to add new stock",
                    content = @Content)
    })
    @PostMapping("/add/{companyCode}")
    public Stock addNewStock(@RequestHeader("e-stock-market-trace-id") String traceID,
                             @RequestHeader("Authorization") String token,
                             @PathVariable("companyCode")String companyCode,
                             @RequestBody Stock stock){
        return stockService.addNewStock(traceID, token, companyCode, stock);
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
    public List<Stock> getListOfStocksForCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                                                 Pageable pageable,
                                                 @PathVariable("companyCode")String companyCode){
        return stockService.getListOfStocksForCompany(traceID, pageable, companyCode);
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
    public Page<Stock> getListOfStocksForCompanyWithinTimeSpan(@RequestHeader("e-stock-market-trace-id") String traceID,
                                                               Pageable pageable,
                                                               @PathVariable("companyCode")String companyCode,
                                                               @PathVariable("startDate")String startDate,
                                                               @PathVariable("endDate")String endDate) throws ParseException {
        return stockService.getListOfStocksForCompanyWithinTimeSpan(traceID, pageable, companyCode, startDate, endDate);
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
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized to delete stocks",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Unauthorized to delete stocks",
                    content = @Content)
    })
    @DeleteMapping("/delete/{companyCode}/")
    public String deleteAllStocksForCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                              @RequestHeader("Authorization") String token,
                              @PathVariable("companyCode") String companyCode) {

        stockService.deleteAllStocksForCompany(traceID, token, companyCode);

        return "All stocks deleted successfully for company: " + companyCode;
    }
}
