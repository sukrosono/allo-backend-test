package com.allo.sukrosono.service;

import com.allo.sukrosono.intf.ResourceAcquire;
import com.allo.sukrosono.model.ResourceType;
import com.allo.sukrosono.model.SupportedCurrencyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupportedCurrencyService implements ResourceAcquire<SupportedCurrencyResponse> {
    private final RestClient restClient;
    private SupportedCurrencyResponse cachedSupportedCurrencyResponse;
    public static final String SUPPORTED_CURRENCY_PATH = "currencies";

    @Override
    public SupportedCurrencyResponse fetch() {
        return new SupportedCurrencyResponse(restClient.get().uri(uri ->
                        uri.path(SUPPORTED_CURRENCY_PATH)
                                .build())
                .retrieve().body(new ParameterizedTypeReference<Map<String, String>>() {
                }));
    }

    @Override
    public String getType() {
        return ResourceType.SUPPORTED_CURRENCIES.name();
    }

    @Override
    public void initCache() {
        cachedSupportedCurrencyResponse = fetch();
        System.out.println("cached: "+ cachedSupportedCurrencyResponse);
    }

    @Override
    public SupportedCurrencyResponse getResource() {
        return cachedSupportedCurrencyResponse;
    }
}
