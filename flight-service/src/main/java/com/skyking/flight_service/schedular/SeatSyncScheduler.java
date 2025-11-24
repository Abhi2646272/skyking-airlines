package com.skyking.flight_service.schedular;

import com.skyking.flight_service.config.FlightProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SeatSyncScheduler {

    private final FlightProperties props;

    @Scheduled(cron = "${application.flight.seat-sync-cron}")
    public void syncSeats() {
        if (!props.isSeatSyncEnable()) return;

        log.info("Running seat sync job...");
        // TODO: Call inventory-service and update seats
    }
}
