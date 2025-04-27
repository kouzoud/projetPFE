package org.example.projetpfe.entity;



import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class WeatherForecast {
    private String location = "Morocco";
    private Instant lastUpdated = Instant.now();
    private List<DailyForecast> forecasts;

    // Helper method to get forecasts by date
    public List<DailyForecast> getForecastsForDate(LocalDate date) {
        return forecasts.stream()
                .filter(f -> f.getForecastDate().equals(date))
                .collect(Collectors.toList());
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<DailyForecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<DailyForecast> forecasts) {
        this.forecasts = forecasts;
    }
}
