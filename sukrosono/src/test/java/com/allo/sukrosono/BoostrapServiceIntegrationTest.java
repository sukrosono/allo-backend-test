package com.allo.sukrosono;

import com.allo.sukrosono.intf.ResourceAcquire;
import com.allo.sukrosono.model.ResourceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BoostrapServiceIntegrationTest {
    @Autowired
    private BoostrapService bootstrapService;
    @Mock
    private BoostrapService mockedBoostrapService;

    ResourceAcquire<?> mockStrategy;
    Map<String, ResourceAcquire<?>> resourceAcquireMap;

    @BeforeEach
    void setup() {
        mockStrategy = mock(ResourceAcquire.class);
        when(mockStrategy.getType()).thenReturn(ResourceType.SUPPORTED_CURRENCIES.name());
        resourceAcquireMap = Map.of(ResourceType.SUPPORTED_CURRENCIES.name(), mockStrategy);
        mockedBoostrapService = new BoostrapService(resourceAcquireMap);
    }

//    to verify the ApplicationRunner successfully initializes and loads the data into
//    the in-memory store before the application context is ready.
    @Test
    void cacheIsLoadedWhenApplicationStarts() {
        Map<String, ResourceAcquire<?>> map = bootstrapService.getAcquireStrategies();

        assertNotNull(map);
        assertEquals(3, map.size());
        map.values().forEach(strategy ->
                assertNotNull(strategy.getResource()) // cache must be initialized
        );
    }

    @Test
    void afterRun_shouldInvokeInitCache() {
        mockedBoostrapService.run();
        verify(mockStrategy).initCache();
    }


    @Test
    void run_Should() {
        mockedBoostrapService.run();
        Assertions.assertEquals(1, mockedBoostrapService.getAcquireStrategies().size()
                , "Should have 1 strategy as setup unit test with 1 mocked strategy");
    }
}