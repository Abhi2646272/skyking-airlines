package com.skyking.flight_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 5)
    private String code; // DEL, BOM

    @Column(nullable = false)
    private String name; // Indira Gandhi Intl

    private String city;
    private String country;
}
