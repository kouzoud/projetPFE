package org.example.projetpfe.service;

import org.example.projetpfe.entity.MeteoDataDTO;
import org.example.projetpfe.entity.PrevisionMeteoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class MeteoService {

    private final PrevisionMeteoRepository repository;

    public MeteoService(PrevisionMeteoRepository repository) {
        this.repository = repository;
    }

    public void importerDonnees(String cheminFichier) throws IOException {
        List<MeteoDataDTO> donnees = NetCDFParser.parse(cheminFichier);

        LocalDate today = LocalDate.now();
        for (MeteoDataDTO data : donnees) {
            PrevisionMeteo prevision = new PrevisionMeteo(
                    data.getTemperature(),
                    data.getVent(),
                    data.getPrecipitation(),
                    today
            );
            repository.save(prevision);
        }
    }
}