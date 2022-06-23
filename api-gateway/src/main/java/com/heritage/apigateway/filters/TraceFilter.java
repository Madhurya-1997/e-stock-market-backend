package com.heritage.apigateway.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Order(1)
@Component
public class TraceFilter implements GlobalFilter {
    private static final Logger logger = LoggerFactory.getLogger(TraceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isTracingIdPresent(requestHeaders)) {
            logger.debug("E-Stock market TRACE-ID found in tracing filter: {}. ",
                    filterUtility.getTraceId(requestHeaders));
        } else {
            String correlationID = generateTracingId();
            exchange = filterUtility.setTraceId(exchange, correlationID);
            logger.debug("E-Stock market TRACE-ID generated in tracing filter: {}.", correlationID);
        }
        return chain.filter(exchange);
    }

    private boolean isTracingIdPresent(HttpHeaders requestHeaders) {
        if (filterUtility.getTraceId(requestHeaders) != null) {
            return true;
        } else {
            return false;
        }
    }

    private String generateTracingId() {
        return java.util.UUID.randomUUID().toString();
    }

}
