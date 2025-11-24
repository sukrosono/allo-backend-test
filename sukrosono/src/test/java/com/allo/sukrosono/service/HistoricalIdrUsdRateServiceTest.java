package com.allo.sukrosono.service;

import com.allo.sukrosono.model.ResourceType;
import com.allo.sukrosono.model.TimeSeriesRatesResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.web.client.RestClient.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@AutoConfigureMockRestServiceServer
class HistoricalIdrUsdRateServiceTest {
    @InjectMocks
    HistoricalIdrUsdRateService historicalIdrUsdRateService;

    @Mock
    Builder builder;
    @Mock
    RestClient restClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RequestHeadersSpec requestHeadersSpec;
    @Mock
    private ResponseSpec responseSpec;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;
    private TimeSeriesRatesResponse timeSeriesRatesResponse;

    @BeforeEach
    void setUp() {
        mockRestServiceServer = MockRestServiceServer.bindTo(builder).build();
        timeSeriesRatesResponse= new TimeSeriesRatesResponse(1, "IDR", "2025-11-17"
        , "2025-11-20", Map.of("2025-11-17", Map.of("USD", 6.0E-5)));

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetch_shouldRequestWithGetAndUsingUrlConfigured() {
        mockRestServiceServer.expect(requestTo("2025-01-01..2025-02-02"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(String.valueOf(timeSeriesRatesResponse), MediaType.APPLICATION_JSON));
        historicalIdrUsdRateService.fetch();
    }

    @Test
    void getType_shouldReturnFromEnumeratedName() {
        Assertions.assertEquals(ResourceType.HISTORICAL_IDR_USD.name(), historicalIdrUsdRateService.getType());
    }

    @Test
    void initCache() {
        when(historicalIdrUsdRateService.fetch()).thenReturn(timeSeriesRatesResponse);
        historicalIdrUsdRateService.initCache();
        Assertions.assertEquals(timeSeriesRatesResponse, historicalIdrUsdRateService.getResource()
        , "should return the same thing from fetch and cache");
    }

    @Test
    void getResource_shouldReturnNullWhenInitCacheNotInvoked() {
        Assertions.assertNull(historicalIdrUsdRateService.getResource());
    }
}