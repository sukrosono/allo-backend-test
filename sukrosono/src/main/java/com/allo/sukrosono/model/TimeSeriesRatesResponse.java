package com.allo.sukrosono.model;

import java.util.Map;

public record TimeSeriesRatesResponse(int amount, String base, String startDate,
                                      String endDate, Map<String, Double> rates) {
}