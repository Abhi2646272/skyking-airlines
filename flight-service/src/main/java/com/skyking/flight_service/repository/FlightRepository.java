package com.skyking.flight_service.repository;

import com.skyking.flight_service.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findBySource_CodeAndDestination_CodeAndDepartureTimeBetween(
            String sourceCode, String destinationCode, LocalDateTime start, LocalDateTime end);

    List<Flight> findByActiveTrue();
}
