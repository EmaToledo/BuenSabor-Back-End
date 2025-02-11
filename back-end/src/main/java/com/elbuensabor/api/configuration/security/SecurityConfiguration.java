package com.elbuensabor.api.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() // Permitir peticiones OPTIONS sin autenticación
                        .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll() // Permitir acceso público a la ruta /api/public
                        .requestMatchers(HttpMethod.GET, "/api/categories/public/filter/catalogue").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/images/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/sell/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/manufactured-products/sell/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/order/public/items").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/filter/manufactured-product").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/filter/product").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/manufactured-products/list-manufactured").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/list-products").permitAll()
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configurar CORS
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:4000")); // Permitir solicitudes desde estas URL
        configuration.setAllowedMethods((Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"))); // Permitir estos métodos HTTP
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type", "X-Get-Header", "Content-Type")); // Permitir estos encabezados
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);

        // Configurar validadores de tokens JWT
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("permissions"); // Configurar el nombre del claim que contiene los permisos
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }
}
