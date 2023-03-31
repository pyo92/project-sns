package com.example.projectsns.dto.request;

import com.example.projectsns.model.entity.Member;

public record SignUpRequest(
        String memberId,
        String password
) {

    public static SignUpRequest of(String memberId, String password) {
        return new SignUpRequest(memberId, password);
    }

    public Member toEntity() {
        return Member.of(memberId, password);
    }
}

