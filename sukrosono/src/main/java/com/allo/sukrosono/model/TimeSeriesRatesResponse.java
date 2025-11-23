package com.allo.sukrosono.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record TimeSeriesRatesResponse(int amount, String base, @JsonProperty("start_date") String startDate,
                                      @JsonProperty("end_date") String endDate, Map<String, Map<String, Double>> rates) {
}