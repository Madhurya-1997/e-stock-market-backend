package com.heritage.stock.services;

import com.heritage.stock.models.Stock;
import com.heritage.stock.models.StockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface StockService {
    public Stock addNewStock(String companyCode, StockRequest stock);
    public List<Stock> getListOfStocksForCompany(Pageable pageable, String companyCode);
    public List<Stock> getListOfStocksForCompanyWithinTimeSpan(Pageable pageable,
                                                               String companyCode,
                                                               String startDate,
                                                               String endDate) throws ParseException;
    public List<Stock> getListOfStocksWithinTimeSpan(Pageable pageable, String startDate, String endDate) throws ParseException;
    public void deleteAllStocksForCompany(String companyCode);
}
