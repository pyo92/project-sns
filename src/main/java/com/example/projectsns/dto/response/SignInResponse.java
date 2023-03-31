package com.example.projectsns.dto.response;

public record SignInResponse(
        String token
) {

    public static SignInResponse of(String token) {
        return new SignInResponse(token);
    }
}
