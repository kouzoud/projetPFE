package org.example.projetpfe.control;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.example.projetpfe.Config.EcmwfClient;
import org.example.projetpfe.entity.DailyForecast;
import org.example.projetpfe.entity.PrevisionMeteo;
import org.example.projetpfe.entity.WeatherForecast;
import org.example.projetpfe.service.WeatherService;
import org.example.projetpfe.util.WeatherServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meteo")
@Slf4j
public class MeteoController {
    @Autowired
    private  WeatherService weatherService;
    private EcmwfClient ecmwfClient;

    public MeteoController(WeatherService weatherService, EcmwfClient ecmwfClient) {
        this.weatherService = weatherService;
        this.ecmwfClient = ecmwfClient;
    }


    @GetMapping("/weather")
    public String testWeatherData() {
        return weatherService.getBasicWeather();
    }
    @GetMapping("/test-connection")
    public ResponseEntity<?> Connection() {
        try {
            // Test 1: Liste des datasets
            String datasets = ecmwfClient.listAvailableDatasets();

            // Test 2: Requête minimale
            String testResponse = ecmwfClient.getWeatherData(
                    "era5", "fc", "0",
                    "2t", "sfc",
                    "32/-12/36/-8", "json",
                    "00"
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "datasets", datasets,
                    "testResponse", testResponse
            ));

        } catch (Exception e) {
            return ResponseEntity.status(503).body(Map.of(
                    "status", "error",
                    "error", e.getMessage(),
                    "details", (e instanceof FeignException)
                            ? ((FeignException)e).contentUTF8()
                            : "Voir les logs serveur"
            ));
        }
    }
    @GetMapping("/connectivity")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Test with the simplest possible request
            String apiResponse = ecmwfClient.listAvailableDatasets();
            response.put("status", "SUCCESS");
            response.put("response", apiResponse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Connection test failed", e);
            response.put("status", "FAILED");
            response.put("error", e.getMessage());
            if (e instanceof FeignException) {
                FeignException fe = (FeignException) e;
                response.put("statusCode", fe.status());
                try {
                    response.put("responseBody", fe.contentUTF8());
                } catch (Exception ex) {
                    response.put("responseBody", "Could not decode error response");
                }
            }
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
    @GetMapping("/datasets")
    public String testDatasets() {
        return ecmwfClient.listAvailableDatasets();
    }
    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new HashMap<>();
        try {
            String apiResponse = ecmwfClient.listAvailableDatasets();
            response.put("status", "UP");
            response.put("ecmwfApi", "ACCESSIBLE");
            response.put("details", "Service is healthy");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Health check failed", e);
            response.put("status", "DOWN");
            response.put("ecmwfApi", "UNREACHABLE");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
    @GetMapping("/previsions")
    public ResponseEntity<?> getPrevisions() {
        try {
            WeatherForecast forecast = weatherService.getMoroccoWeatherForecast();
            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            return handleWeatherException(e);

        }
    }
    /*@GetMapping("/previsions/{region}")
    public ResponseEntity<?> getPrevisionsParRegion(
            @PathVariable String region,
            @RequestParam(required = false) String ville) {

        try {
            WeatherForecast forecast;

            if (ville != null) {
                forecast = weatherService.getForecastByVille(region, ville);
            } else {
                forecast = weatherService.getForecastByRegion(region);
            }

            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            return handleWeatherException(e);
        }
    }*/
    private ResponseEntity<Map<String, Object>> handleWeatherException(Exception e) {
        Map<String, Object> response = new HashMap<>();

        if (e instanceof WeatherServiceException) {
            response.put("status", "error");
            response.put("code", HttpStatus.SERVICE_UNAVAILABLE.value());
            response.put("message", "Service météo indisponible");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } else {
            response.put("status", "error");
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Erreur interne du serveur");
            return ResponseEntity.internalServerError().body(response);
        }
    }



    @GetMapping("/morocco")
    public ResponseEntity<?> getCurrentForecast() {
        try {
            WeatherForecast forecast = weatherService.getMoroccoWeatherForecast();

            // Transform to DTO if needed
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("location", forecast.getLocation());
            response.put("lastUpdated", forecast.getLastUpdated());
            response.put("forecasts", forecast.getForecasts().stream()
                    .map(this::convertToForecastDto)
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "Internal server error",
                            "details", "Please try again later"
                    ));
        }
    }
    private Map<String, Object> convertToForecastDto(DailyForecast forecast) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("time", forecast.getForecastTime());
        dto.put("temperature", Math.round(forecast.getTemperature() * 10) / 10.0);
        dto.put("precipitation", Math.round(forecast.getPrecipitation()));
        dto.put("windSpeed", Math.round(forecast.getWindSpeed() * 3.6)); // m/s to km/h
        dto.put("windDirection", forecast.getWindDirection());
        return dto;
    }



}