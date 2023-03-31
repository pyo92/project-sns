package com.example.projectsns.dto.response;

import com.example.projectsns.model.MemberRole;
import com.example.projectsns.model.entity.Member;

public record SignUpResponse(
        String memberId,
        MemberRole role
) {

    public static SignUpResponse of(String memberId, MemberRole role) {
        return new SignUpResponse(memberId, role);
    }

    public static SignUpResponse from(Member entity) {
        return SignUpResponse.of(entity.getMemberId(), entity.getRole());
    }
}
