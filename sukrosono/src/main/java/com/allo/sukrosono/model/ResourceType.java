package com.allo.sukrosono.model;

public enum ResourceType {
    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORYCAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private String action;

    ResourceType(String name) {
        action = name;
    }

}
