package com.example.projectsns.model.type;

import lombok.Getter;

public enum UserRole {

    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    @Getter
    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
