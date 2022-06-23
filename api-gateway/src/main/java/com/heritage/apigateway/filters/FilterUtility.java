package com.heritage.apigateway.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class FilterUtility {

    public static final String TRACE_ID = "e-stock-market-trace-id";

    public String getTraceId(HttpHeaders requestHeaders) {
        if (requestHeaders.get(TRACE_ID) != null) {
            List<String> requestHeaderList = requestHeaders.get(TRACE_ID);
            return requestHeaderList.stream().findFirst().get();
        } else {
            return null;
        }
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    public ServerWebExchange setTraceId(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, TRACE_ID, correlationId);
    }

}