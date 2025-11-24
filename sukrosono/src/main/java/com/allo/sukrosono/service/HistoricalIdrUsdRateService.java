package com.allo.sukrosono.service;

import com.allo.sukrosono.intf.ResourceAcquire;
import com.allo.sukrosono.model.ResourceType;
import com.allo.sukrosono.model.TimeSeriesRatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class HistoricalIdrUsdRateService implements ResourceAcquire<TimeSeriesRatesResponse> {
    private final RestClient restClient;

    private TimeSeriesRatesResponse cachedTimeSeriesRatesResponse;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${frankfurter.base-currency}")
    private String baseCurrency;
    @Value("${frankfurter.target-currency}")
    private String targetCurrency;

    @Override
    public TimeSeriesRatesResponse fetch() {
        LocalDate now = LocalDate.from(LocalDateTime.now());
        LocalDate endDate = now.minusWeeks(1L);
        return restClient.get().uri(uri ->
                        uri.path(String.format("%s..%s", dateTimeFormatter.format(endDate), dateTimeFormatter.format(now)))
                        .queryParam("base", baseCurrency)
                        .queryParam("symbols", targetCurrency).build())
                .retrieve().body(TimeSeriesRatesResponse.class);
    }

    @Override
    public String getType() {
        return ResourceType.HISTORICAL_IDR_USD.name();
    }

    @Override
    public void initCache() {
        cachedTimeSeriesRatesResponse= fetch();
    }

    @Override
    public TimeSeriesRatesResponse getResource() {
        return cachedTimeSeriesRatesResponse;    }
}
