package com.heritage.stock.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class LoadBalancerConfiguration {
//    @Bean
//    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(ConfigurableApplicationContext context) {
//        return ServiceInstanceListSupplier.builder()
//                .withBlockingDiscoveryClient()
//                .withSameInstancePreference()
//                .build(context);
//    }
}
