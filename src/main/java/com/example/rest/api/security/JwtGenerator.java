package com.example.rest.api.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtGenerator {

    private final JWSSigner signer;
    private final Clock clock = Clock.systemUTC();

    //should be made configurable
    private final Duration expiryPeriod = Duration.ofMinutes(15);
    private static final String secretKey = "secret";

    public JwtGenerator() throws KeyLengthException {
        byte[] decodedBytes = Base64.getDecoder().decode(secretKey);
        signer = new MACSigner(Arrays.copyOf(decodedBytes, 256));
    }

    public String generate(UserDetails userDetails) throws JOSEException {
        JWTClaimsSet jwtClaimsSet = jwtClaimsSet(userDetails);
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private JWTClaimsSet jwtClaimsSet(UserDetails userDetails) {
        return new JWTClaimsSet.Builder()
            .issuer("API")
            .subject(userDetails.getEmail())
            .issueTime(Date.from(clock.instant()))
            .expirationTime(Date.from(clock.instant().plus(expiryPeriod)))
            .jwtID(UUID.randomUUID().toString())
            .claim("roles", userDetails.getRoles())
            .build();
    }
}
