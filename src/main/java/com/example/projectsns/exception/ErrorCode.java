package com.example.projectsns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    DUPLICATED_MEMBER_ID(HttpStatus.CONFLICT, "Member ID is duplicated"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member is not founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    ;

    private HttpStatus status;
    private String message;
}
