package org.example.projetpfe.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecast {
    private Instant forecastTime;  // Changed from 'time' to 'forecastTime'
    private double temperature;
    private double precipitation;
    private double windSpeed;
    private String windDirection;

    // Additional helper method if needed
    public LocalDate getForecastDate() {
        return forecastTime.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Instant getForecastTime() {
        return forecastTime;
    }

    public void setForecastTime(Instant forecastTime) {
        this.forecastTime = forecastTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }
}
