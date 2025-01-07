package com.example.demo.config;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    UserService userService;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Désactive la protection CSRF, utile pour les APIs REST sans état
        http.csrf(AbstractHttpConfigurer::disable)
                // Désactive la gestion CORS automatique
                .cors(AbstractHttpConfigurer::disable)
                // Configure la gestion des sessions pour être sans état (stateless)
                .sessionManagement(se -> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure les règles d'autorisation des requêtes HTTP
                .authorizeHttpRequests(auth -> auth
                                // Autorise l'accès public à l'endpoint "/api/users/login"
                                .requestMatchers("/api/users/login",
                                        "/swagger-ui/**",        // Swagger UI
                                        "/v3/api-docs/**",       // Endpoints OpenAPI JSON
                                        "/swagger-ui.html"       // Fichier principal de Swagger UI
                                ).permitAll()
                                // Exige une authentification pour toutes les autres requêtes
                                .anyRequest().authenticated()
                                //  .anyRequest().permitAll()
                )

                // Utilise le service UserService pour charger les utilisateurs
                .userDetailsService(userService)
                // Ajoute le filtre JWT avant le filtre UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Retourne la configuration HTTP finalisée
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
