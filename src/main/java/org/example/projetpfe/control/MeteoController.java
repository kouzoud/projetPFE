package org.example.projetpfe.control;

import org.example.projetpfe.service.MeteoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/meteo")
public class MeteoController {

    private final MeteoService service;

    public MeteoController(MeteoService service) {
        this.service = service;
    }

    @PostMapping("/import")
    public String importerNetCDF(@RequestParam("file") MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("netcdf-", ".nc");
            file.transferTo(tempFile.toFile());

            service.importerDonnees(tempFile.toString());
            return "Importation réussie 🚀";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur d'importation : " + e.getMessage();
        }
    }
}