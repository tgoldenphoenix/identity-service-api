package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.AuthenticationRequest;
import com.devteria.identityservice.dto.request.IntrospectRequest;
import com.devteria.identityservice.dto.response.AuthenticationResponse;
import com.devteria.identityservice.dto.response.IntrospectResponse;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j// a logging framework
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
  UserRepository userRepository;

  // Khai bao @NonFinal de lombok khong inject vao constructor
  @NonFinal
  @Value("${jwt.signerKey}")// doc bien tu file .yaml
  protected String SIGNER_KEY;

  public IntrospectResponse introspect(IntrospectRequest request)
    throws JOSEException, ParseException {
    var token = request.getToken();

    JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    var verified = signedJWT.verify(verifier);

    return IntrospectResponse.builder()
             .valid(verified && expiryTime.after(new Date()))
             .build();
  }

  // (!!!) neu quen khong ghi public là no default private
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var user = userRepository.findByUsername(request.getUsername())
                 .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

    var token = generateToken(user);

    return AuthenticationResponse.builder()
             .token(token)
             .authenticated(true)
             .build();
  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

    // data inside payload is called "claim"
    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                                  .subject(user.getUsername())
                                  .issuer("devteria.com")
                                  .issueTime(new Date())
                                  .expirationTime(new Date(
                                    // expire after one hour
                                    Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                                  ))
                                  // custom claims
                                  // scope == roles
                                  .claim("scope", buildScope(user))
                                  .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(header, payload);

    try {
      jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Cannot create token", e);
      throw new RuntimeException(e);
    }
  }

  private String buildScope(User user){
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles()))
      user.getRoles().forEach(stringJoiner::add);
//      user.getRoles().forEach(s -> stringJoiner.add(s));

    // test code
//    user.getRoles().forEach(role -> {
//      System.out.println("AuthenticationService > buildScope");
//      System.out.println(role);
//    });

    return stringJoiner.toString();
  }
}
