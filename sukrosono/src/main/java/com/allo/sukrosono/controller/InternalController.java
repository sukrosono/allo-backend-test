package com.allo.sukrosono.controller;

import com.allo.sukrosono.BoostrapService;
import com.allo.sukrosono.model.ResourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("api/finance")
@RequiredArgsConstructor
public class InternalController {
    private final BoostrapService boostrapService;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> index(@PathVariable String resourceType) {
        ResourceType requestedResourceType;
        try {
            requestedResourceType = ResourceType.valueOf(resourceType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Resource requested is not found");
        }
        var response = boostrapService.getAcquireStrategies().get(requestedResourceType.name()).getResource();
        return ResponseEntity.ok(response);
    }
}
