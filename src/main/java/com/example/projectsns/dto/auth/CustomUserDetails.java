package com.example.projectsns.dto.auth;

import com.example.projectsns.model.entity.User;
import com.example.projectsns.model.type.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Spring security - 사용자 인증 정보 관련 dto
 */
public record CustomUserDetails(
        String email,
        String password,
        String nickname,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt
) implements UserDetails {

    public static CustomUserDetails of(String email, String password, String nickname, UserRole role, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        return new CustomUserDetails(email, password, nickname, role, createdAt, modifiedAt, deletedAt);
    }

    public static CustomUserDetails from(User entity) {
        return CustomUserDetails.of(
                entity.getEmail(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return deletedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return deletedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return deletedAt == null;
    }

    @Override
    public boolean isEnabled() {
        return deletedAt == null;
    }
}
