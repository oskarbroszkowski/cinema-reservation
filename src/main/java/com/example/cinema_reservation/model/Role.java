package com.example.cinema_reservation.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    @JsonValue
    public String getAuthority() {
        return name();
    }

    @JsonCreator
    public static Role fromString(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
