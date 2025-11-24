package com.skyking.flight_service.controller;

import com.skyking.flight_service.dto.AirportDto;
import com.skyking.flight_service.entity.Airport;
import com.skyking.flight_service.repository.AirportRepository;
import com.skyking.flight_service.utils.GenericMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportRepository airportRepository;
    private final ModelMapper mapper;



    @PostMapping
    public ResponseEntity<AirportDto> create(@Valid @RequestBody AirportDto dto) {
        Airport airport = mapper.map(dto, Airport.class);
        Airport saved = airportRepository.save(airport);
        return ResponseEntity.ok(mapper.map(saved, AirportDto.class));
    }

    @GetMapping
    public ResponseEntity<List<AirportDto>> getAll() {
        List<AirportDto> list = airportRepository.findAll().stream()
                .map(a -> mapper.map(a, AirportDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportDto> get(@PathVariable("id") Long id) {
        Airport a = airportRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(mapper.map(a, AirportDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportDto> update(@PathVariable("id") Long id, @Valid @RequestBody AirportDto dto) {
        Airport airport = airportRepository.findById(id).orElseThrow();
        airport.setCode(dto.getCode());
        airport.setName(dto.getName());
        airport.setCity(dto.getCity());
        airport.setCountry(dto.getCountry());
        Airport saved = airportRepository.save(airport);
        return ResponseEntity.ok(mapper.map(saved, AirportDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        airportRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
