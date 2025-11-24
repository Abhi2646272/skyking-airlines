package com.skyking.flight_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirlineDto {
    private Long id;
    private String code;
    private String name;
}
