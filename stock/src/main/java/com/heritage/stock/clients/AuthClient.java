package com.heritage.stock.clients;

import com.heritage.stock.config.LoadBalancerConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${auth.feign.name}")
//@LoadBalancerClient(name = "${auth.feign.name}", configuration = LoadBalancerConfiguration.class)
public interface AuthClient {

    @GetMapping(value="/auth/validate")
    public AuthResponse verifyUser(@RequestHeader("Authorization") String token);
}
