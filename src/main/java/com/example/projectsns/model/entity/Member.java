package com.example.projectsns.model.entity;

import com.example.projectsns.dto.request.SignInRequest;
import com.example.projectsns.dto.request.SignUpRequest;
import com.example.projectsns.model.AuditingFields;
import com.example.projectsns.model.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
@Entity
public class Member extends AuditingFields {

    @Id
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    public static Member of(String memberId, String password, MemberRole role) {
        return new Member(memberId, password, role);
    }

    public static Member of(String memberId, String password) {
        return Member.of(memberId, password, MemberRole.MEMBER);
    }

    public static Member from(SignUpRequest dto) {
        return Member.of(dto.memberId(), dto.password());
    }

    public static Member from(SignInRequest dto) {
        return Member.of(dto.memberId(), dto.password());
    }
}
