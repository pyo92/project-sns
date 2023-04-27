package com.example.projectsns.dto.request;

import com.example.projectsns.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record JoinRequest(
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$", message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        String email,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8장 이상, 20자 이하여야 합니다.")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        String password,

        @Pattern(regexp = "^[a-zA-Z0-9_.]{5,20}$", message = "닉네임은 영문, 숫자, 특수문자 \'_\' 와 \'.\' 만 사용 가능합니다.")
        @Size(min = 5, max = 20, message = "닉네임은 5자 이상, 20자 이하여야 합니다.")
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        String nickname
) {

    public static JoinRequest of(String email, String password, String nickname) {
        return new JoinRequest(email, password, nickname);
    }

    public static JoinRequest of() {
        return JoinRequest.of(null, null, null);
    }

    public User toEntity() {
        return User.from(this);
    }
}
