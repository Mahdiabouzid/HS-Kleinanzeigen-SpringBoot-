package de.hs.da.hskleinanzeigen.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class Swagger {
    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info();
        info.setTitle("HS Kleinanzeigen");
        info.setDescription("Plattform zum Verwalten von Kleinanzeigen");
        info.setVersion("v2.0.22");
        openAPI.setInfo(info);
        return openAPI;
    }
}
