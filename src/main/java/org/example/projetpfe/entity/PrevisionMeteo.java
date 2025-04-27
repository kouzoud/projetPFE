package org.example.projetpfe.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class PrevisionMeteo {
    @Id
    @GeneratedValue
    private Long id;

    private Double temperature;
    private Double vent;
    private Double precipitation;
    private LocalDate date;

    public PrevisionMeteo() {}

    public PrevisionMeteo(Double temperature, Double vent, Double precipitation, LocalDate date) {
        this.temperature = temperature;
        this.vent = vent;
        this.precipitation = precipitation;
        this.date = date;
    }

    // Getters and Setters
}

