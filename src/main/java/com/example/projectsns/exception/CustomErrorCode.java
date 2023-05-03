package com.example.projectsns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception 에 사용될 error code 목록
 */
@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    /**
     * server side error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid JWT Token."),

    /**
     * client side error
     */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다. 다시 확인 해주세요."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    INVALID_LOGIN_INFO(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원 정보를 찾을 수 없습니다."),
    ;
    @Getter
    private final HttpStatus status;

    @Getter
    private final String message;
}
