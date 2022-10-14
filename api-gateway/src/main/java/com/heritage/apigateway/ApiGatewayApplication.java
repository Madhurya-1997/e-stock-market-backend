package com.heritage.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@SpringBootApplication
@EnableEurekaClient
@CrossOrigin
public class ApiGatewayApplication {

	@Value("${market.company.uri}")
	private String COMPANY_URI;
	@Value("${market.stock.uri}")
	private String STOCK_URI;


	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
						.route(p -> p
								.path("/api/v1.0/market/auth-service/**")
								.filters(f -> f.rewritePath("/api/v1.0/market/auth-service/(?<segment>.*)",
										"/${segment}")
										.addResponseHeader("X-Response-Time", new Date().toString()))
								.uri("lb://auth-service")).
						route(p -> p
								.path("/api/v1.0/market/company-service/**")
								.filters(f -> f.rewritePath("/api/v1.0/market/company-service/(?<segment>.*)",
										"/${segment}")
										.addResponseHeader("X-Response-Time", new Date().toString()))
								.uri(COMPANY_URI)).
						route(p -> p
								.path("/api/v1.0/market/stock-service/**")
								.filters(f -> f.rewritePath("/api/v1.0/market/stock-service/(?<segment>.*)",
										"/${segment}")
										.addResponseHeader("X-Response-Time", new Date().toString()))
								.uri(STOCK_URI)).build();
	}
}
