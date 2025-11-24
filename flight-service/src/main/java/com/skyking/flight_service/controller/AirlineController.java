package com.skyking.flight_service.controller;

import com.skyking.flight_service.dto.AirlineDto;
import com.skyking.flight_service.service.AirlineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @PostMapping
    public ResponseEntity<AirlineDto> create(@Valid @RequestBody AirlineDto dto) {
        return ResponseEntity.ok(airlineService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AirlineDto>> getAll() {
        return ResponseEntity.ok(airlineService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirlineDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(airlineService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirlineDto> update(@PathVariable Long id, @Valid @RequestBody AirlineDto dto) {
        return ResponseEntity.ok(airlineService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        airlineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
