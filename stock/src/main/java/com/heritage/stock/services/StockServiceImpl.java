package com.heritage.stock.services;

import com.heritage.stock.clients.CompanyClient;
import com.heritage.stock.exceptions.CompanyCodeMismatchException;
import com.heritage.stock.exceptions.CompanyNotFoundException;
import com.heritage.stock.exceptions.NotAllowedException;
import com.heritage.stock.models.Company;
import com.heritage.stock.models.Stock;
import com.heritage.stock.models.StockRequest;
import com.heritage.stock.repository.StockRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class StockServiceImpl implements StockService{

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);


    @Autowired
    private StockRepository stockRepository;
//    @Autowired
//    private CompanyClient companyClient;


    @Override
//    @Retry(name = "retryAddNewStock", fallbackMethod = "addNewStockFallback")
    public Stock addNewStock(String traceID, String companyCode, StockRequest stock) {

        logger.debug("Invoking addNewStock service with trace ID: " + traceID);

//        Company company = companyClient.findCompany(traceID, companyCode);
//        logger.debug("Existing company is: " + company + " with trace ID: " + traceID);
//        if (company == null) {
//            throw new CompanyNotFoundException();
//        }

        if (!stock.getCompanyCode().equalsIgnoreCase(companyCode)) {
            throw new CompanyCodeMismatchException();
        }

        Stock newStock = new Stock();
        newStock.setStockDuration(stock.getDuration());
        newStock.setStockPrice(stock.getPrice());
        newStock.setCompanyCode(companyCode);
        newStock.setEndDate(addHoursToJavaUtilDate(new Date(), stock.getDuration()));
        return stockRepository.save(newStock);
    }

    @Override
    public List<Stock> getListOfStocksForCompany(String traceID, Pageable pageable, String companyCode) {
        logger.debug("Invoking getListOfStocksForCompany service with trace ID: " + traceID);

        List<Stock> stocks = stockRepository.findAllByCompanyCode(companyCode);
        return stockRepository.findAllByCompanyCode(companyCode);
    }

    @Override
    public Page<Stock> getListOfStocksForCompanyWithinTimeSpan(String traceID, Pageable pageable, String companyCode, String startDate, String endDate) throws ParseException {
        logger.debug("Invoking getListOfStocksForCompanyWithinTimeSpan service with trace ID: " + traceID);

        return stockRepository.findAllByCompanyCodeAndCreatedAtBetween(pageable,
                companyCode,
                new SimpleDateFormat("dd-MM-yyyy").parse(startDate),
                new SimpleDateFormat("dd-MM-yyyy").parse(endDate));
    }

    @Override
    public Page<Stock> getListOfStocksWithinTimeSpan(String traceID, Pageable pageable, String startDate, String endDate) throws ParseException {
        logger.debug("Invoking getListOfStocksForCompanyWithinTimeSpan service with trace ID: " + traceID);

        return stockRepository.findAllByCreatedAtBetween(pageable,
                new SimpleDateFormat("dd-MM-yyyy").parse(startDate),
                new SimpleDateFormat("dd-MM-yyyy").parse(endDate));
    }


    @Override
//    @Retry(name = "retryDeleteAllStocksForCompany", fallbackMethod = "deleteAllStocksForCompanyFallback")
    public void deleteAllStocksForCompany(String traceID, String companyCode) {
        logger.debug("Invoking deleteAllStocksForCompany service with trace ID: " + traceID);

        stockRepository.deleteAllByCompanyCode(companyCode);
    }

    private Date addHoursToJavaUtilDate(Date date, Integer hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    /**
     * Handling Auth Client Failure for Stock Deletion
     * @param traceID
     * @param token
     * @return
     */
//    private Stock deleteAllStocksForCompanyFallback(String traceID, String token, String companyCode, Throwable t) {
//        Stock fallbackStock = new Stock();
//        fallbackStock.setFallbackMessage("Authentication service is not responding or user token has expired. Please try again later !! Trace ID: " + traceID);
//        return fallbackStock;
//    }

    /**
     * Handling Client Failures for Stock Addition
     * @param traceID
     * @param token
     * @return
     */
//    private Stock addNewStockFallback(String traceID, String token, String companyCode, Stock stock, Throwable t) {
//        Stock fallbackStock = new Stock();
//        fallbackStock.setFallbackMessage("Company or Auth service is not responding. Please try again later !! Trace ID: " + traceID);
//        return fallbackStock;
//    }


}
