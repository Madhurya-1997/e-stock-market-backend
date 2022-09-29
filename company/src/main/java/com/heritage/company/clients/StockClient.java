package com.heritage.company.clients;

import com.heritage.company.models.Stock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "${stock.feign.name}")
public interface StockClient {

    @GetMapping("/stock/get/{companyCode}")
    public List<Stock> getListOfStocksForCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                                                 @PathVariable("companyCode")String companyCode);
    @DeleteMapping("/stock/delete/{companyCode}/")
    public String deleteAllStocksForCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
                                            @PathVariable("companyCode") String companyCode);
}
