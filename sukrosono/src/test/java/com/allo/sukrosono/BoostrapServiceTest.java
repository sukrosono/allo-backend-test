package com.allo.sukrosono;

import com.allo.sukrosono.intf.ResourceAcquire;
import com.allo.sukrosono.model.ResourceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoostrapServiceTest {
    private BoostrapService boostrapService;

    ResourceAcquire<?> mockStrategy;
    Map<String, ResourceAcquire<?>> resourceAcquireMap;

    @BeforeEach
    void setup() {
        mockStrategy = mock(ResourceAcquire.class);
        when(mockStrategy.getType()).thenReturn(ResourceType.SUPPORTED_CURRENCIES.name());
        resourceAcquireMap = Map.of(ResourceType.SUPPORTED_CURRENCIES.name(), mockStrategy);
        boostrapService = new BoostrapService(resourceAcquireMap);
    }

    @Test
    void populateCache_shouldInvokeInitCache() {
        boostrapService.populateCache();
        verify(mockStrategy).initCache();
    }

    @Test
    void getAcquireStrategies_Should() {
        boostrapService.populateCache();
        Assertions.assertEquals(1, boostrapService.getAcquireStrategies().size()
                , "Should have 1 strategy as setup unit test with 1 mocked strategy");
    }

}