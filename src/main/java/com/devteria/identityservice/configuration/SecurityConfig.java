package com.devteria.identityservice.configuration;

import com.devteria.identityservice.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity// optional
// authorization based on method, not endpoints
@EnableMethodSecurity
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
                                           // authorization base on engpoints
                                           // only admin can getAllUsers
//                                           .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())
                                           .anyRequest().authenticated());

    // all other endpoints, user must provide a valid jwt token
    httpSecurity.oauth2ResourceServer(oauth2 ->
                                        oauth2.jwt(jwtConfigurer ->
                                                     jwtConfigurer.decoder(jwtDecoder())
                                                       .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                        )
    );

    // default spring security will turn on this csrf config
    // :: is short-hand of the lambda method (Method References)
    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    return httpSecurity.build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter(){
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    // default là "SCOPE_"
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

    return jwtAuthenticationConverter;
  }

  @Bean
  JwtDecoder jwtDecoder(){
    SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
    return NimbusJwtDecoder
             .withSecretKey(secretKeySpec)
             .macAlgorithm(MacAlgorithm.HS512)
             .build();
  }

  // bean duoc dua vao application context
  // de xu dung o nhiều noi khac nhau
  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(10);
  }
}
