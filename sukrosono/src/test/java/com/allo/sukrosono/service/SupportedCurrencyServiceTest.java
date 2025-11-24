package com.allo.sukrosono.service;

import com.allo.sukrosono.model.ResourceType;
import com.allo.sukrosono.model.SupportedCurrencyResponse;
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
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
class SupportedCurrencyServiceTest {
    @InjectMocks
    SupportedCurrencyService supportedCurrencyService;

    @Mock
    RestClient.Builder builder;
    @Mock
    RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;
    private SupportedCurrencyResponse supportedCurrencyResponse;
    private Map<String, String> bodyResponse;

    @BeforeEach
    void setUp() {
        mockRestServiceServer = MockRestServiceServer.bindTo(builder).build();
        bodyResponse = Map.of("IDR", "1234", "AUD", "321");
        supportedCurrencyResponse = new SupportedCurrencyResponse(bodyResponse);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetch_shouldRequestWithGetAndUsingUrlConfigured() {
        mockRestServiceServer.expect(requestTo(SupportedCurrencyService.SUPPORTED_CURRENCY_PATH))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(String.valueOf(supportedCurrencyResponse), MediaType.APPLICATION_JSON));
        supportedCurrencyService.fetch();
    }

    @Test
    void getType_shouldReturnFromEnumeratedName() {
        Assertions.assertEquals(ResourceType.SUPPORTED_CURRENCIES.name(), supportedCurrencyService.getType());
    }

    @Test
    void initCache() {
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(bodyResponse);
        supportedCurrencyService.initCache();
        Assertions.assertEquals(supportedCurrencyResponse, supportedCurrencyService.getResource()
                , "should be the same");
    }

    @Test
    void getResource_shouldReturnNullWhenInitCacheNotInvoked() {
        Assertions.assertNull(supportedCurrencyService.getResource());
    }
}