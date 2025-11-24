package com.skyking.flight_service.service.impl;

import com.skyking.flight_service.config.FlightProperties;
import com.skyking.flight_service.dto.FlightDto;
import com.skyking.flight_service.dto.request.FlightCreateRequest;
import com.skyking.flight_service.entity.Airline;
import com.skyking.flight_service.entity.Airport;
import com.skyking.flight_service.entity.Flight;
import com.skyking.flight_service.exception.BadRequestException;
import com.skyking.flight_service.exception.ResourceNotFoundException;
import com.skyking.flight_service.repository.AirlineRepository;
import com.skyking.flight_service.repository.AirportRepository;
import com.skyking.flight_service.repository.FlightRepository;
import com.skyking.flight_service.service.FlightService;
import com.skyking.flight_service.utils.FlightSearchCache;
import com.skyking.flight_service.utils.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final FlightProperties props;
    private final FlightSearchCache cache;
    private final ModelMapper mapper;



    @Override
    @Transactional
    public FlightDto create(FlightCreateRequest req) {
        Airline airline = airlineRepository.findById(req.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found id=" + req.getAirlineId()));
        Airport source = airportRepository.findById(req.getSourceAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Source airport not found id=" + req.getSourceAirportId()));
        Airport dest = airportRepository.findById(req.getDestinationAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination airport not found id=" + req.getDestinationAirportId()));

        if (!req.getArrivalTime().isAfter(req.getDepartureTime())) {
            throw new BadRequestException("Arrival time must be after departure time");
        }

        Flight flight = Flight.builder()
                .flightNumber(req.getFlightNumber())
                .airline(airline)
                .source(source)
                .destination(dest)
                .departureTime(req.getDepartureTime())
                .arrivalTime(req.getArrivalTime())
                .totalSeats(req.getTotalSeats())
                .availableSeats(req.getAvailableSeats() == null ? req.getTotalSeats() : req.getAvailableSeats())
                .price(req.getPrice())
                .active(true)
                .build();

        return mapper.map(flightRepository.save(flight), FlightDto.class);
    }

    @Override
    @Transactional
    public FlightDto update(Long id, FlightCreateRequest req) {
        Flight existing = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found id=" + id));

        // fetch relations
        Airline airline = airlineRepository.findById(req.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found id=" + req.getAirlineId()));
        Airport source = airportRepository.findById(req.getSourceAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Source airport not found id=" + req.getSourceAirportId()));
        Airport dest = airportRepository.findById(req.getDestinationAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination airport not found id=" + req.getDestinationAirportId()));

        existing.setFlightNumber(req.getFlightNumber());
        existing.setAirline(airline);
        existing.setSource(source);
        existing.setDestination(dest);
        existing.setDepartureTime(req.getDepartureTime());
        existing.setArrivalTime(req.getArrivalTime());
        existing.setTotalSeats(req.getTotalSeats());
        existing.setAvailableSeats(req.getAvailableSeats());
        existing.setPrice(req.getPrice());

        return mapper.map(flightRepository.save(existing), FlightDto.class);
    }

    @Override
    public FlightDto getById(Long id) {
        return flightRepository.findById(id)
                .map(f -> mapper.map(f, FlightDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found id=" + id));
    }

    @Override
    public List<FlightDto> getAll() {
        return flightRepository.findAll().stream()
                .map(f -> mapper.map(f, FlightDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        flightRepository.deleteById(id);
    }


    @Override
    public List<FlightDto> search(String fromCode, String toCode, LocalDate date) {

        String key = fromCode + "-" + toCode + "-" + date.toString();

        if (props.isEnableSearchCache()) {
            List<FlightDto> cached = cache.get(key);
            if (cached != null) return cached;
        }

        List<FlightDto> flights = flightRepository.findBySource_CodeAndDestination_CodeAndDepartureTimeBetween(
                        fromCode, toCode, date.atStartOfDay(), date.atTime(LocalTime.MAX)
                ).stream()
                .limit(props.getMaxFlightResults())
                .map(f -> mapper.map(f, FlightDto.class))
                .toList();

        if (props.isEnableSearchCache()) {
            cache.put(key, flights, props.getSearchCacheTtlMs());
        }

        return flights;
    }

}
