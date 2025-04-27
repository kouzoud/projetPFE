package org.example.projetpfe.service;


import feign.Feign;
import feign.FeignException;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projetpfe.Config.EcmwfAuthInterceptor;
import org.example.projetpfe.Config.EcmwfClient;
import org.example.projetpfe.entity.WeatherForecast;
import org.example.projetpfe.entity.WeatherRequest;
import org.example.projetpfe.util.WeatherServiceException;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;


@Service
@Slf4j
public class WeatherService {

    // Configuration directe des credentials ECMWF
    private final String apiKey = "790584e1439004e516bdf9be1c97e77e";
    private final String email = "aymanee341@gmail.com";
    private final String apiUrl = "https://api.ecmwf.int/v1";

    // Paramètres par défaut
    private final String defaultDataset = "era5";
    private final String defaultType = "fc";
    private final String defaultSteps = "0/12/24";
    private final String defaultParams = "2t/tp/10u/10v";
    private final String defaultLevtype = "sfc";
    private final String defaultArea = "32/-12/36/-8"; // Coordonnées du Maroc
    private final String defaultFormat = "json";

    private final EcmwfClient ecmwfClient;

    public WeatherService() {
        this.ecmwfClient = Feign.builder()
                .contract(new SpringMvcContract()) // <<< C'est ça la solution magique
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(new EcmwfAuthInterceptor(apiKey, email))
                .target(EcmwfClient.class, apiUrl);
    }
    public String testConnection() {
        try {
            // Test simple de connexion
            String response = ecmwfClient.listAvailableDatasets();
            log.info("Connexion réussie à l'API ECMWF");
            return response;
        } catch (FeignException e) {
            log.error("ECHEC Connexion - Headers envoyés:", e.request().headers());
            throw new RuntimeException("Erreur 403 - Vérifiez vos credentials", e);
        }
    }

    public String getBasicWeather() {
        try {
            return ecmwfClient.getWeatherData(
                    "era5",         // Dataset
                    "fc",          // Type
                    "0",           // Step
                    "2t",          // Param
                    "sfc",         // Level type
                    "32/-12/36/-8", // Zone Maroc
                    "json",        // Format
                    "00"          // Time
            );
        } catch (FeignException e) {
            log.error("Requête échouée - Détails:", e.contentUTF8());
            throw new RuntimeException("Erreur lors de la récupération des données", e);
        }
    }



    public WeatherForecast getMoroccoWeatherForecast() {
        try {
            log.info("Tentative de connexion à l'API ECMWF avec la clé: {}...", apiKey.substring(0, 4));

            // D'abord vérifier la connexion de base
            String testResponse = ecmwfClient.listAvailableDatasets();
            log.debug("Test de connexion réussi, datasets disponibles: {}", testResponse);

            // Ensuite faire la vraie requête
            String response = ecmwfClient.getWeatherData(
                    "era5",                  // Utiliser era5 au lieu de interim
                    "fc",
                    "0",                     // Commencer simple avec juste le temps présent
                    "2t",                    // D'abord juste la température
                    "sfc",
                    "32/-12/36/-8",
                    "json",
                    "00"
            );

            log.debug("Réponse brute de l'API: {}", response);
            return parseEcmwfResponse(response);

        } catch (FeignException e) {
            String errorDetails = "Statut: " + e.status() + " - Erreur: " + e.contentUTF8();
            log.error("Échec de la requête ECMWF. {}", errorDetails);
            throw new WeatherServiceException("Impossible de contacter le service météo. " + errorDetails);
        }
    }


    /*public WeatherForecast getForecastByRegion(String region) {
        String areaCoordinates = getCoordinatesForRegion(region);

        WeatherRequest request = new WeatherRequest();
        request.setDataset(defaultDataset);
        request.setType(defaultType);
        request.setStep(defaultSteps);
        request.setParam(defaultParams);
        request.setLevtype(defaultLevtype);
        request.setArea(areaCoordinates);
        request.setFormat(defaultFormat);
        request.setTime("00/12");

        String response = ecmwfClient.getWeatherData(request);

        return parseEcmwfResponse(response);
    }*/


    /*public WeatherForecast getForecastByVille(String region, String ville) {
        String areaCoordinates = getCoordinatesForVille(region, ville);

        WeatherRequest request = new WeatherRequest();
        request.setDataset(defaultDataset);
        request.setType(defaultType);
        request.setStep("0");  // Seulement les prévisions immédiates
        request.setParam("2t"); // Température seulement
        request.setLevtype(defaultLevtype);
        request.setArea(areaCoordinates);
        request.setFormat(defaultFormat);
        request.setTime("00");

        String response = ecmwfClient.getWeatherData(request);

        return parseEcmwfResponse(response);
    }*/


    private WeatherForecast parseEcmwfResponse(String jsonResponse) {
        // Implémentation du parsing de la réponse JSON
        WeatherForecast forecast = new WeatherForecast();
        forecast.setLocation("Maroc");
        forecast.setLastUpdated(Instant.now());

        // Logique de parsing ici...

        return forecast;
    }

    private String getCoordinatesForRegion(String region) {
        // Mapping des régions aux coordonnées GPS
        switch (region.toLowerCase()) {
            case "casablanca":
                return "33.57/-7.58/33.60/-7.55";
            case "rabat":
                return "34.01/-6.85/34.03/-6.80";
            // Ajouter d'autres régions...
            default:
                return defaultArea;
        }
    }

    private String getCoordinatesForVille(String region, String ville) {
        // Mapping plus précis pour les villes
        if ("casablanca".equalsIgnoreCase(region) && "ain diab".equalsIgnoreCase(ville)) {
            return "33.57/-7.70/33.58/-7.69";
        }
        // Autres villes...
        return getCoordinatesForRegion(region);
    }
}