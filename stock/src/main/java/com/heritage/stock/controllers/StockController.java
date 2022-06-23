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
    public Stock addNewStock(@RequestHeader("Authorization") String token,
                             @PathVariable("companyCode")String companyCode,
                             @RequestBody Stock stock){
        return stockService.addNewStock(token, companyCode, stock);
    }

    @GetMapping("/get/{companyCode}")
    public Page<Stock> getListOfStocksForCompany(Pageable pageable, @PathVariable("companyCode")String companyCode){
        return stockService.getListOfStocksForCompany(pageable, companyCode);
    }

    @GetMapping("/get/{companyCode}/{startDate}/{endDate}")
    public Page<Stock> getListOfStocksForCompanyWithinTimeSpan(Pageable pageable,
                                                               @PathVariable("companyCode")String companyCode,
                                                               @PathVariable("startDate")String startDate,
                                                               @PathVariable("endDate")String endDate){
        return stockService.getListOfStocksForCompanyWithinTimeSpan(pageable, companyCode, startDate, endDate);
    }
}
