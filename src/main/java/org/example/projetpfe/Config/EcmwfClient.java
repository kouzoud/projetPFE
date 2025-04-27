package org.example.projetpfe.Config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ecmwf-client",
        url = "https://api.ecmwf.int/v1",
        configuration = EcmwfFeignConfig.class
)
public interface EcmwfClient {

    @GetMapping("/datasets")
    String listAvailableDatasets();

    @GetMapping("/data/{dataset}/type/{type}/step/{step}")
    String getWeatherData(
            @org.springframework.web.bind.annotation.PathVariable("dataset") String dataset,
            @org.springframework.web.bind.annotation.PathVariable("type") String type,
            @org.springframework.web.bind.annotation.PathVariable("step") String step,
            @RequestParam("param") String param,
            @RequestParam("levtype") String levtype,
            @RequestParam("area") String area,
            @RequestParam("format") String format,
            @RequestParam("time") String time
    );
}
