package com.example.projectsns.dto;

import com.example.projectsns.model.entity.User;

/**
 * server side 에서 타 도메인 회원 검증을 위해 사용될 user dto
 */
public record UserDto(
        String email
) {

    public static UserDto of(String email) {
        return new UserDto(email);
    }

    public User toEntity() {
        return User.from(this);
    }
}
