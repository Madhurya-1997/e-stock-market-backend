package com.heritage.stock.clients;

import com.heritage.stock.config.LoadBalancerConfiguration;
import com.heritage.stock.models.Company;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

//@FeignClient(name = "${company.feign.name}")
public interface CompanyClient {
//    @GetMapping("/company/info/{code}")
//    public Company findCompany(@RequestHeader("e-stock-market-trace-id") String traceID,
//                               @PathVariable("code") String code);
}
