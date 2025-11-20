package com.allo.sukrosono.model;

import java.util.Map;

public record SeriesRateResponse(Map<String, Map<String, Double>> nestedRates) {
}
