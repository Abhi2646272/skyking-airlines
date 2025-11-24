package com.skyking.flight_service.service.impl;

import com.skyking.flight_service.dto.AirlineDto;
import com.skyking.flight_service.entity.Airline;
import com.skyking.flight_service.exception.ResourceNotFoundException;
import com.skyking.flight_service.repository.AirlineRepository;
import com.skyking.flight_service.service.AirlineService;
import com.skyking.flight_service.utils.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;
    private final ModelMapper mapper;



    @Override
    public AirlineDto create(AirlineDto dto) {
        Airline airline = mapper.map(dto, Airline.class);
        Airline saved = airlineRepository.save(airline);
        return mapper.map(saved, AirlineDto.class);
    }

    @Override
    public AirlineDto update(Long id, AirlineDto dto) {
        Airline existing = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found id=" + id));
        existing.setCode(dto.getCode());
        existing.setName(dto.getName());
        return mapper.map(airlineRepository.save(existing), AirlineDto.class);
    }

    @Override
    public AirlineDto getById(Long id) {
        return airlineRepository.findById(id)
                .map(a -> mapper.map(a, AirlineDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found id=" + id));
    }

    @Override
    public List<AirlineDto> getAll() {
        return airlineRepository.findAll().stream()
                .map(a -> mapper.map(a, AirlineDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        airlineRepository.deleteById(id);
    }
}
