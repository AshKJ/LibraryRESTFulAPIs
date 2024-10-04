package com.example.rest.api.security;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@JsonSerialize
@NoArgsConstructor
@Getter
@Setter
public class UserDetails {

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private final Set<String> roles = new HashSet<>();
}