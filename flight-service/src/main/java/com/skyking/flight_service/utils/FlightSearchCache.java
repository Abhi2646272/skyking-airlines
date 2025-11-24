package com.skyking.flight_service.utils;

import com.skyking.flight_service.dto.FlightDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
@AllArgsConstructor
public class FlightSearchCache {

    private final Map<String, CacheEntry> cache = new HashMap<>();

    public void put(String key, List<FlightDto> data, long ttlMs) {
        cache.put(key, new CacheEntry(data, Instant.now().plusMillis(ttlMs)));
    }

    public List<FlightDto> get(String key) {
        CacheEntry entry = cache.get(key);

        if (entry == null || Instant.now().isAfter(entry.expiry())) {
            cache.remove(key);
            return null;
        }
        return entry.data();
    }

    private record CacheEntry(List<FlightDto> data, Instant expiry) {}
}
