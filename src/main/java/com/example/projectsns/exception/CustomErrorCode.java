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
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT Token is invalid."),

    /**
     * client side error
     */
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "입력하신 이메일 주소가 이미 사용중입니다. 다른 이메일을 사용해주세요."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "입력하신 닉네임이 이미 사용중입니다. 다른 닉네임을 사용해주세요."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "입력하신 이메일 주소는 찾을 수 없습니다. 다시 한 번 확인해주세요."),
    INVALID_LOGIN_INFO(HttpStatus.UNAUTHORIZED, "입력하신 로그인 정보가 올바르지 않습니다. 다시 한 번 확인해주세요."),
    ;
    @Getter
    private final HttpStatus status;

    @Getter
    private final String message;
}
