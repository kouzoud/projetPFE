package org.example.projetpfe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.projetpfe.Config") // ou le package où se trouve EcmwfClient

public class ProjetPfeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetPfeApplication.class, args);
    }

}
