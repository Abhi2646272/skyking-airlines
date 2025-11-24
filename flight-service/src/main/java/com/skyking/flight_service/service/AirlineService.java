package com.skyking.flight_service.service;


import com.skyking.flight_service.dto.AirlineDto;

import java.util.List;

public interface AirlineService {
    AirlineDto create(AirlineDto dto);
    AirlineDto update(Long id, AirlineDto dto);
    AirlineDto getById(Long id);
    List<AirlineDto> getAll();
    void delete(Long id);
}
