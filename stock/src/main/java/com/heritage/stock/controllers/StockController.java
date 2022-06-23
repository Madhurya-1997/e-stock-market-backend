package com.heritage.stock.controllers;

import com.heritage.stock.models.Stock;
import com.heritage.stock.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/")
    public String welcome() {
        return "<h1>Welcome to Stock Microservice !!</h1>";
    }


    @PostMapping("/add/{companyCode}")
    public Stock addNewStock(@RequestHeader("e-stock-market-trace-id") String traceID,
                             @RequestHeader("Authorization") String token,
                             @PathVariable("companyCode")String companyCode,
                             @RequestBody Stock stock){
        return stockService.addNewStock(traceID, token, companyCode, stock);
    }

    @GetMapping("/get/{companyCode}")
    public Page<Stock> getListOfStocksForCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                                                 Pageable pageable,
                                                 @PathVariable("companyCode")String companyCode){
        return stockService.getListOfStocksForCompany(traceID, pageable, companyCode);
    }

    @GetMapping("/get/{companyCode}/{startDate}/{endDate}")
    public Page<Stock> getListOfStocksForCompanyWithinTimeSpan(@RequestHeader("e-stock-market-trace-id") String traceID,
                                                               Pageable pageable,
                                                               @PathVariable("companyCode")String companyCode,
                                                               @PathVariable("startDate")String startDate,
                                                               @PathVariable("endDate")String endDate){
        return stockService.getListOfStocksForCompanyWithinTimeSpan(traceID, pageable, companyCode, startDate, endDate);
    }
}
