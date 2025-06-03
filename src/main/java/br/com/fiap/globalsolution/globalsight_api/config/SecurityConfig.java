package br.com.fiap.globalsolution.globalsight_api.config;

import br.com.fiap.globalsolution.globalsight_api.exception.GlobalExceptionHandler;
import br.com.fiap.globalsolution.globalsight_api.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper; // Para escrever a resposta de erro JSON

    // DTO interno para a resposta de erro padronizada, movido para cá para encapsulamento
    // ou pode ser movido para uma classe separada no pacote exception se preferir.
    public record ApiErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path,
            List<String> details
    ) {
        public ApiErrorResponse(HttpStatus httpStatus, String message, String path, List<String> details) {
            this(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path, details);
        }
        public ApiErrorResponse(HttpStatus httpStatus, String message, String path) {
            this(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path, null);
        }
    }


    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ObjectMapper objectMapper) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(
                                "/api/swagger-ui/**",
                                "/api/v1/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**", // Adicionado para cobrir recursos da UI do Swagger
                                "/v3/api-docs/**"  // Adicionado para cobrir recursos da especificação OpenAPI v3
                        ).permitAll()

                        // Endpoints que requerem autenticação
                        .requestMatchers("/api/simulations/**").authenticated()
                        .requestMatchers("/api/drone/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/history/**").hasRole("USER") // Exemplo: só ADMIN pode criar histórico
                        .requestMatchers(HttpMethod.PUT, "/api/history/**").hasRole("USER")   // Exemplo: só ADMIN pode atualizar histórico
                        .requestMatchers(HttpMethod.DELETE, "/api/history/**").hasRole("USER")// Exemplo: só ADMIN pode deletar histórico
                                       
                        // Permitindo que qualquer usuário AUTENTICADO realize CRUD em /api/history
                        .requestMatchers(HttpMethod.POST, "/api/history/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/history/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/history/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/history/**").authenticated() // Leitura também requer autenticação
                        .anyRequest().authenticated() // Todas as outras requisições precisam de autenticação
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            // Usando o record ApiErrorResponse definido nesta classe
                            ApiErrorResponse errorResponse = new ApiErrorResponse(
                                    HttpStatus.UNAUTHORIZED,
                                    "Autenticação necessária. Faça login para acessar este recurso.",
                                    request.getRequestURI(),
                                    null
                            );
                            objectMapper.writeValue(response.getOutputStream(), errorResponse);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            // Usando o record ApiErrorResponse definido nesta classe
                            ApiErrorResponse errorResponse = new ApiErrorResponse(
                                    HttpStatus.FORBIDDEN,
                                    "Acesso negado. Você não tem permissão para acessar este recurso.",
                                    request.getRequestURI(),
                                    null
                            );
                            objectMapper.writeValue(response.getOutputStream(), errorResponse);
                        })
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Em produção, especifique as origens permitidas em vez de "*"
        configuration.addAllowedOriginPattern("*"); // Ex: "http://localhost:3000" para um frontend React local
        configuration.addAllowedMethod("*"); // Ou especifique métodos: "GET", "POST", "PUT", "DELETE", "OPTIONS"
        configuration.addAllowedHeader("*"); // Ou especifique cabeçalhos: "Authorization", "Content-Type"
        configuration.setAllowCredentials(true);
        // configuration.setMaxAge(3600L); // Opcional: tempo de cache para pre-flight requests

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
