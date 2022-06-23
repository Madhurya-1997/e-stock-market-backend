package com.heritage.stock.services;

import com.heritage.stock.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {
    public Stock addNewStock(String token,
                             String companyCode,
                             Stock stock);
    public Page<Stock> getListOfStocksForCompany(Pageable pageable,
                                                 String companyCode);
    public Page<Stock> getListOfStocksForCompanyWithinTimeSpan(Pageable pageable,
                                                               String companyCode,
                                                               String startDate,
                                                               String endDate);
}
