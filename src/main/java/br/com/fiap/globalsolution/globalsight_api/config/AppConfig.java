package br.com.fiap.globalsolution.globalsight_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "GlobalSIGHT API",
                version = "v1.0.0", // Você pode usar a versão do seu application.properties aqui se preferir
                description = "API para o projeto GlobalSIGHT de análise e previsão de desastres."
                // Você pode adicionar mais informações como contact, license, etc.
        )
)
@SecurityScheme(
        name = "bearerAuth", // Este nome DEVE ser o mesmo usado em @SecurityRequirement nos controllers
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "Insira o token JWT no formato: Bearer {seu_token}"
)

public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}