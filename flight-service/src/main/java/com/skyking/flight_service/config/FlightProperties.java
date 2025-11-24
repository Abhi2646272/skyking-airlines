package com.skyking.flight_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application.flight")
public class FlightProperties {
    private String defaultCurrency;
    private boolean enableSearchCache;
    private long searchCacheTtlMs;
    private int maxFlightResults;
    private boolean seatSyncEnable;
    private String seatSyncCron;
}
