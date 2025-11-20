package com.allo.sukrosono.model;

import java.util.Map;

public record RatesResponse(int amount, String base, String date,
                            Map<String, Double> rates) {
}
