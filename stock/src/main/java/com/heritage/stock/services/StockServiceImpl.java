package com.heritage.stock.services;

import com.heritage.stock.clients.AuthClient;
import com.heritage.stock.clients.AuthResponse;
import com.heritage.stock.clients.CompanyClient;
import com.heritage.stock.exceptions.CompanyNotFoundException;
import com.heritage.stock.exceptions.UserNotFoundException;
import com.heritage.stock.exceptions.UserTokenExpiredException;
import com.heritage.stock.models.Company;
import com.heritage.stock.models.Stock;
import com.heritage.stock.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class StockServiceImpl implements StockService{

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);


    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private AuthClient authClient;
    @Autowired
    private CompanyClient companyClient;


    @Override
    public Stock addNewStock(String traceID, String token, String companyCode, Stock stock) {

        logger.debug("Invoking addNewStock service with trace ID: " + traceID);

        Company company = companyClient.findCompany(traceID, companyCode);
        logger.debug("Existing company is: " + company + " with trace ID: " + traceID);

        AuthResponse authResponse = authClient.verifyUser(traceID, token);
        logger.debug("Existing user is: " + authResponse.getUsername() + " with trace ID: " + traceID);

//        try {
//            authResponse = authClient.verifyUser(token);
//        } catch(Exception e) {
//            throw new UserTokenExpiredException();
//        }

        if (authResponse == null) {
            throw new UserNotFoundException();
        }
        if (company == null) {
            throw new CompanyNotFoundException();
        }

//        Stock newStock = new Stock();
//        newStock.setStockPrice(stock.getStockPrice());
        stock.setCompanyCode(companyCode);
        stock.setEndDate(addHoursToJavaUtilDate(new Date(), stock.getStockDuration()));
        return stockRepository.save(stock);
    }

    // pagination:
    // https://medium.com/javarevisited/spring-boot-mongodb-searching-and-pagination-1a6c1802024a
    @Override
    public Page<Stock> getListOfStocksForCompany(String traceID, Pageable pageable, String companyCode) {
        return null;
    }

    @Override
    public Page<Stock> getListOfStocksForCompanyWithinTimeSpan(String traceID, Pageable pageable, String companyCode, String startDate, String endDate) {
        logger.debug("Invoking getListOfStocksForCompanyWithinTimeSpan service with trace ID: " + traceID);

        return stockRepository.findAll(pageable);
    }

    private Date addHoursToJavaUtilDate(Date date, Integer hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

}
