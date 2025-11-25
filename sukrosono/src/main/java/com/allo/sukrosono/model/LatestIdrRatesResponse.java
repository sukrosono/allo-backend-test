package com.allo.sukrosono.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class  LatestIdrRatesResponse {
    private int amount;
    private String base;
    private String date;
    @JsonProperty("USD_BuySpread_IDR")
    private Double UsdBuySpreadIdr;
    private Map<String, Double> rates;
}
