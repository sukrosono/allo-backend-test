package com.allo.sukrosono;

import com.allo.sukrosono.intf.ResourceAcquire;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BoostrapService implements CommandLineRunner {
    private final Map<String, ResourceAcquire<?>> acquireStrategies;

    public BoostrapService(Map<String, ResourceAcquire<?>> definedStrategy) {
        acquireStrategies = definedStrategy.values().stream()
                .collect(Collectors.toUnmodifiableMap(
                ResourceAcquire::getType,
                Function.identity()
        ));
    }

    @PostConstruct
    public void populateCache() {
        acquireStrategies.values()
                .forEach(ResourceAcquire::initCache);
    }

    @Override
    public void run(String... args) {
    }

    public Map<String, ResourceAcquire<?>> getAcquireStrategies() {
        return acquireStrategies;
    }
}
