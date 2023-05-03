package com.example.projectsns.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Custom exception - 프로젝트 전역에서 사용하기 위해 정의한 예외 class
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomException extends RuntimeException {

    private CustomErrorCode customErrorCode;
    private String message;
    private Object requestDto; //입력 form 에서 오류 발생 시, 입력값을 그대로 바인딩 하기 위한 dto

    public static CustomException of(CustomErrorCode customErrorCode, Object requestDto) {
        return new CustomException(customErrorCode, customErrorCode.getMessage(), requestDto);
    }

    public static CustomException of(CustomErrorCode customErrorCode) {
        return CustomException.of(customErrorCode, null);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
