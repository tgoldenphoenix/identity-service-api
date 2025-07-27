package com.devteria.identityservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity// optional
public class SecurityConfig {

  private final String[] PUBLIC_ENDPOINTS = {
    "/users",
    "/auth/token",
    "/auth/introspect"
  };

  @Value("${jwt.signerKey}")
  private String signerKey;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    // Config end points: add user, register get token & introspect thi ai cung co the access duoc
    httpSecurity.authorizeHttpRequests(request ->
                                         request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                                           .anyRequest().authenticated());

    // all other endpoints, user must provide a valid jwt token
    httpSecurity.oauth2ResourceServer(oauth2 ->
                                        oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
    );

    // default spring security will turn on this csrf config
    // :: is short-hand of the lambda method (Method References)
    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    return httpSecurity.build();
  }

  @Bean
  JwtDecoder jwtDecoder(){
    SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
    return NimbusJwtDecoder
             .withSecretKey(secretKeySpec)
             .macAlgorithm(MacAlgorithm.HS512)
             .build();
  }
}
