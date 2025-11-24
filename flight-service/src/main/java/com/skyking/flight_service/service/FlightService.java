package com.skyking.flight_service.service;


import com.skyking.flight_service.dto.FlightDto;
import com.skyking.flight_service.dto.request.FlightCreateRequest;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    FlightDto create(FlightCreateRequest request);
    FlightDto update(Long id, FlightCreateRequest request);
    FlightDto getById(Long id);
    List<FlightDto> getAll();
    void delete(Long id);

    List<FlightDto> search(String fromCode, String toCode, LocalDate date);
}
