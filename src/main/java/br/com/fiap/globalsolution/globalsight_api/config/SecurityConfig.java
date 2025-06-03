package br.com.fiap.globalsolution.globalsight_api.config;

import br.com.fiap.globalsolution.globalsight_api.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
                .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF pois usaremos JWT (API stateless)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuração de CORS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API Stateless
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/swagger-ui/**", "/api/v1/api-docs/**", "/swagger-ui.html").permitAll()
                        // .requestMatchers(HttpMethod.GET, "/api/disasters/history/**").permitAll() // Ex: Se histórico for público

                        // Endpoints que requerem autenticação (ou roles específicas com @PreAuthorize nos controllers)
                        .requestMatchers("/api/simulations/**").authenticated()
                        .requestMatchers("/api/drone/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/history/**").hasRole("ADMIN") // Exemplo: só ADMIN pode criar histórico
                        .requestMatchers(HttpMethod.PUT, "/api/history/**").hasRole("ADMIN")   // Exemplo: só ADMIN pode atualizar histórico
                        .requestMatchers(HttpMethod.DELETE, "/api/history/**").hasRole("ADMIN")// Exemplo: só ADMIN pode deletar histórico
                        .anyRequest().authenticated() // Todas as outras requisições precisam de autenticação
                );

        // Adiciona o filtro JWT antes do filtro de autenticação padrão do Spring
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Permite todas as origens (para desenvolvimento). Em produção, restrinja!
        configuration.addAllowedMethod("*"); // Permite todos os métodos HTTP
        configuration.addAllowedHeader("*"); // Permite todos os cabeçalhos
        configuration.setAllowCredentials(true); // Permite credenciais (cookies, authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica para todos os caminhos
        return source;
    }
}

