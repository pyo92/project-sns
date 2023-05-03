package com.example.projectsns.model.entity;

import com.example.projectsns.dto.UserDto;
import com.example.projectsns.dto.request.JoinRequest;
import com.example.projectsns.model.AuditingFields;
import com.example.projectsns.model.type.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where email=?")
@Where(clause = "deleted_at is NULL")
@Entity
public class User extends AuditingFields {

    @Column(nullable = false, unique = true, length = 30)
    @Id
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ToString.Exclude
    @OrderBy(value = "createdAt ASC")
    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public static User of(String email, String password, String nickname, UserRole role, List<Post> posts) {
        return new User(email, password, nickname, role, posts);
    }

    public static User of(String email) {
        return User.of(email, null, null, null, null);
    }

    public static User from(JoinRequest dto) {
        return User.of(dto.email(), dto.password(), dto.nickname(), UserRole.ROLE_USER, List.of());
    }

    public static User from(UserDto dto) {
        return User.of(dto.email());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return this.email != null && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email);
    }
}
