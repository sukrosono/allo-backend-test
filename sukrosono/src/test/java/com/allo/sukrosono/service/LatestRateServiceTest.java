package com.allo.sukrosono.service;

import com.allo.sukrosono.model.LatestIdrRatesResponse;
import com.allo.sukrosono.model.ResourceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@AutoConfigureMockRestServiceServer
class LatestRateServiceTest {
    @InjectMocks
    LatestRateService latestRateService;

    @Mock
    RestClient.Builder builder;
    @Mock
    RestClient restClient;
    @Value("${spring.application.final-factor}")
    private Double finalFactor;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;
    private LatestIdrRatesResponse latestIdrRatesResponse;

    @BeforeEach
    void setup() {
        mockRestServiceServer = MockRestServiceServer.bindTo(builder).build();
        latestIdrRatesResponse = new LatestIdrRatesResponse();

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetch_shouldRequestWithGetAndUsingUrlConfigured() {
        mockRestServiceServer.expect(requestTo("2025-01-01..2025-02-02"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(String.valueOf(latestIdrRatesResponse), MediaType.APPLICATION_JSON));
    }

    @Test
    void getType_shouldReturnFromEnumeratedName() {
        Assertions.assertEquals(ResourceType.LATEST_IDR_RATES.name(), latestRateService.getType());
    }

    @Test
    void initCache_shouldInteroperateFetch() {
        ReflectionTestUtils.setField(
                latestRateService,
                "finalFactor",
                Double.valueOf(0.05)
        );
        // necessary stub
        ReflectionTestUtils.setField(
                latestRateService,
                "targetCurrency",
                "USD"
        );

        latestIdrRatesResponse.setAmount(2);
        latestIdrRatesResponse.setBase("IDR");
        latestIdrRatesResponse.setDate("2025-01-01");
        latestIdrRatesResponse.setRates(Map.of("USD", 6.0e-05));
        latestIdrRatesResponse.setUsdBuySpreadIdr(150000d);

        when(responseSpec.body(LatestIdrRatesResponse.class)).thenReturn(latestIdrRatesResponse);
        com.allo.sukrosono.model.LatestIdrRatesResponse fetchResponse = latestRateService.fetch();
        Assertions.assertNotNull(fetchResponse);

        System.out.println("fetch response: "+ fetchResponse);
        Assertions.assertEquals(latestIdrRatesResponse, fetchResponse,
                "should saved cached by the fetch call");
    }

    @Test
    void getResource_shouldReturnNullWhenInitCacheNotInvoked() {
        Assertions.assertNull(latestRateService.getResource(), "should have no value");
    }
}