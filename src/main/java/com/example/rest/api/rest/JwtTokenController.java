package com.example.rest.api.rest;

import com.example.rest.api.security.JwtGenerator;
import com.example.rest.api.security.UserDetails;
import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class JwtTokenController {

    private final JwtGenerator jwtGenerator;

    @GetMapping(path = "/jwt")
    public String generateJwt(UserDetails userDetails) throws JOSEException {
        return jwtGenerator.generate(userDetails);
    }
}
