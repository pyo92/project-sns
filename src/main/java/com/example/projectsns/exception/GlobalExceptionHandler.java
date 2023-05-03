package com.example.projectsns.exception;

import com.example.projectsns.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

/**
 * Exception handler - 프로젝트 전역에서 발생하는 예외 처리
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Runtime exception - server side 에서 정의하지 않은 예외 처리
     * TODO: "/error" 페이지를 별도로 구성 - ResponseEntity 반환 내용 출력 및 이전 페이지로 돌아가기 기능 개발
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> exceptionHandler(RuntimeException e) {
        log.error("Error occurs: {}", e.getMessage());

        return ResponseEntity.status(CustomErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(Response.error("ERROR", e.getMessage()));
    }

    /**
     * Custom exception - server side 에서 직접 정의한 예외 처리
     */
    @ExceptionHandler(CustomException.class)
    public ModelAndView exceptionHandler(CustomException e, HttpServletRequest request) {
        //binding result reject
        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "bindingResult");
        bindingResult.reject("ERROR", e.getMessage());

        log.error("Error occurs: {}", e.getMessage());

        //작업 중이던 view 로 다시 반환하기 위한 ModelAndView 바인딩
        ModelAndView mav = new ModelAndView(request.getRequestURI()); //작업 중이던 view 이름으로 세팅
        mav.addObject("errors", bindingResult.getAllErrors()); //에러 내용을 모델에 바인딩

        //입력했던 form value 를 그대로 바인딩하기 위해 request dto 를 model 에 바인딩
        StringBuilder sb = new StringBuilder(e.getRequestDto().getClass().getSimpleName());
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        mav.addObject(sb.toString(), e.getRequestDto()); //request dto 를 모델에 바인딩

        //해당 예외의 http status code 세팅
        mav.setStatus(e.getCustomErrorCode().getStatus());

        return mav;
    }
}
