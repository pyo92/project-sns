package com.example.projectsns.dto.request;

public record LoginRequest(
        String email,
        String password
) {

    public static LoginRequest of(String email, String password) {
        return new LoginRequest(email, password);
    }

    public static LoginRequest of(String email) {
        return LoginRequest.of(email, null);
    }

    public static LoginRequest of() {
        return LoginRequest.of(null, null);
    }
}
