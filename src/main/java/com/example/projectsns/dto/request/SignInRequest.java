package com.example.projectsns.dto.request;

import com.example.projectsns.model.entity.Member;

public record SignInRequest(
        String memberId,
        String password
) {

    public static SignInRequest of(String memberId, String password) {
        return new SignInRequest(memberId, password);
    }

    public Member toEntity() {
        return Member.of(memberId, password);
    }
}

