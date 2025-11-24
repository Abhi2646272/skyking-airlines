package com.skyking.flight_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirportDto {
    private Long id;
    private String code;
    private String name;
    private String city;
    private String country;
}
