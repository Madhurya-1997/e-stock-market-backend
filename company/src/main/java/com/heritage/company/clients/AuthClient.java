package com.heritage.company.clients;

import com.heritage.company.config.LoadBalancerConfiguration;
import com.heritage.company.controllers.AuthFallbackController;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${auth.feign.name}")
//@LoadBalancerClient(name = "${auth.feign.name}", configuration = LoadBalancerConfiguration.class)
public interface AuthClient {


    @GetMapping(value="/auth/validate")
    public AuthResponse verifyUser(@RequestHeader("e-stock-market-trace-id") String traceID,
                                   @RequestHeader("Authorization") String token);
}
