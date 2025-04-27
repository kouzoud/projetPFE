package org.example.projetpfe.Config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.example.projetpfe.util.WeatherServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EcmwfFeignConfig {

    //@Value("${ecmwf.api.key}")
    private String apiKey="790584e1439004e516bdf9be1c97e77e";

    //@Value("${ecmwf.api.email}")
    private String email="aymanee341@gmail.com";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + apiKey);
            requestTemplate.header("From", email);
            requestTemplate.header("Accept", "application/json");
            log.debug("Added ECMWF auth headers to request");
            requestTemplate.header("X-ECMWF-Key", apiKey);
            requestTemplate.header("X-ECMWF-Email", email);

        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            String errorMsg = String.format("ECMWF API Error - Status: %s, Body: %s",
                    response.status(),
                    response.body() != null ? response.body().toString() : "null");

            log.error(errorMsg);
            return new WeatherServiceException(errorMsg);
        };
    }
}