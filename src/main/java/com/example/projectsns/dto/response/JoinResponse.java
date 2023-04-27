package com.example.projectsns.dto.response;

import com.example.projectsns.model.type.UserRole;
import com.example.projectsns.model.entity.User;

public record JoinResponse(
        String email,
        UserRole role
) {

    public static JoinResponse of(String name, UserRole role) {
        return new JoinResponse(name, role);
    }

    public static JoinResponse from(User entity) {
        return JoinResponse.of(entity.getEmail(), entity.getRole());
    }
}
