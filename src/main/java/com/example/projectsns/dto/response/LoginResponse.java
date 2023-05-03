package com.example.projectsns.dto.response;

public record LoginResponse(
        String jwt
) {

    public static LoginResponse of(String jwt) {
        return new LoginResponse(jwt);
    }
}
