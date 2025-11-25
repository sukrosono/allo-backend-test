package com.allo.sukrosono;

import com.allo.sukrosono.intf.ResourceAcquire;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoostrapServiceIntegrationTest {
    @Autowired
    private BoostrapService bootstrapService;

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
}