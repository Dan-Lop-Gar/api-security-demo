package com.example.resourceserver.config;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    // Lee el issuer-uri desde variables de entorno o application.yml
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        //.requestMatchers("/admin/**").hasRole("ADMIN")
                        //.requestMatchers("/admin/**").hasAuthority("SCOPE_write")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())   // 401
                        .accessDeniedHandler(new CustomAccessDeniedHandler())             // 403
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        // =============================
        // JWS — comenta esto para usar JWE
        //Local authserver
//        return JwtDecoders.fromIssuerLocation("http://localhost:8080");
        //Keyclock
//        return JwtDecoders.fromIssuerLocation("http://localhost:8080/realms/mi-empresa");
//        return JwtDecoders.fromIssuerLocation("http://host.docker.internal:8080/realms/mi-empresa");//manual image
//        return JwtDecoders.fromIssuerLocation("http://keycloak:8080/realms/mi-empresa");//compose
        return JwtDecoders.fromIssuerLocation(issuerUri);//from variable
        // =============================

        // =============================
        // JWE — descomenta esto para usar JWE
//         RSAKey rsaKey = RsaKeyStore.getRsaKey(); // la misma clase del Auth Server
//         return new JwtCustomDecoder(rsaKey);
        // =============================
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return jwtConverter;
    }
}
