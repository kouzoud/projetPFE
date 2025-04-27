package org.example.projetpfe.Config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class EcmwfAuthInterceptor implements RequestInterceptor {

    private final String apiKey;
    private final String email;

    public EcmwfAuthInterceptor(String apiKey, String email) {
        this.apiKey = apiKey;
        this.email = email;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", apiKey);
        template.header("From", email);
        template.header("Accept", "application/json");
    }
}
