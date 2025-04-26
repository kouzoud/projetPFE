package org.example.projetpfe.entity;

public class WeatherForecast {
    private String date;
    private double temperature;
    private double precipitation;
    private double windSpeed;

    // Constructeurs, Getters et Setters
    public WeatherForecast() {}

    public WeatherForecast(String date, double temperature, double precipitation, double windSpeed) {
        this.date = date;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.windSpeed = windSpeed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
