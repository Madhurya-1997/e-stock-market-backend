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

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private AuthClient authClient;
    @Autowired
    private CompanyClient companyClient;


    @Override
    public Stock addNewStock(String token, String companyCode, Stock stock) {
        Company company = companyClient.findCompany(companyCode);
        AuthResponse authResponse = authClient.verifyUser(token);

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
    public Page<Stock> getListOfStocksForCompany(Pageable pageable, String companyCode) {
        return null;
    }

    @Override
    public Page<Stock> getListOfStocksForCompanyWithinTimeSpan(Pageable pageable, String companyCode, String startDate, String endDate) {
        return null;
    }

    private Date addHoursToJavaUtilDate(Date date, Integer hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

}
