package com.heritage.stock.services;

import com.heritage.stock.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface StockService {
    public Stock addNewStock(String traceID,
                             String token,
                             String companyCode,
                             Stock stock);
    public List<Stock> getListOfStocksForCompany(String traceID,
                                                 Pageable pageable,
                                                 String companyCode);
    public Page<Stock> getListOfStocksForCompanyWithinTimeSpan(String traceID,
                                                               Pageable pageable,
                                                               String companyCode,
                                                               String startDate,
                                                               String endDate) throws ParseException;
    public Page<Stock> getListOfStocksWithinTimeSpan(String traceID, Pageable pageable, String startDate, String endDate) throws ParseException;
    public void deleteAllStocksForCompany(String traceID, String token, String companyCode);
}
