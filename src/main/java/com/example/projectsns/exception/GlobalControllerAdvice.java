package com.example.projectsns.exception;

import com.example.projectsns.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> exceptionHandler(RuntimeException e) {
        log.error("Error occurs: {}", e.getMessage());
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }

    @ExceptionHandler(ProjectSnsException.class)
    public ResponseEntity<?> exceptionHandler(ProjectSnsException e) {
        log.error("Error occurs: {}", e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }
}
