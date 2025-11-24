package com.skyking.flight_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightCreateRequest {
    @NotBlank
    private String flightNumber;

    @NotNull
    private Long airlineId;

    @NotNull
    private Long sourceAirportId;

    @NotNull
    private Long destinationAirportId;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @Min(1)
    private Integer totalSeats;

    @Min(0)
    private Integer availableSeats;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;
}
