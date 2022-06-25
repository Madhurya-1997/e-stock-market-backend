package com.heritage.company.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CompanyProperties {
    private String message;
    private String buildVersion;
    private Map<String, String> mailDetails;
    private List<String> stockExchange;
}
