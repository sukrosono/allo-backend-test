package com.allo.sukrosono.service;

import com.allo.sukrosono.intf.ResourceAcquire;
import com.allo.sukrosono.model.LatestIdrRatesResponse;
import com.allo.sukrosono.model.ResourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LatestRateService implements ResourceAcquire<LatestIdrRatesResponse> {
    private final RestClient restClient;

    private LatestIdrRatesResponse cachedRatesResponse;

    @Value("${spring.application.final-factor}")
    private Double finalFactor;
    @Value("${frankfurter.base-currency}")
    private String baseCurrency;
    @Value("${frankfurter.target-currency}")
    private String targetCurrency;

    @Override
    public LatestIdrRatesResponse fetch() {
        var response = restClient.get()
                .uri(uri -> uri
                        .path("latest")
                        .queryParam("base", baseCurrency).build())
                .retrieve().body(LatestIdrRatesResponse.class);
        double usdBuySpreadIdr = (1/ Objects.requireNonNull(response).getRates().get(targetCurrency)) * (1 + finalFactor.doubleValue());
        response.setUsdBuySpreadIdr(usdBuySpreadIdr);
        return response;
    }

    @Override
    public String getType() {
        return ResourceType.LATEST_IDR_RATES.name();
    }

    @Override
    public void initCache() {
        cachedRatesResponse= fetch();
    }

    @Override
    public LatestIdrRatesResponse getResource() {
        return cachedRatesResponse;
    }
}
