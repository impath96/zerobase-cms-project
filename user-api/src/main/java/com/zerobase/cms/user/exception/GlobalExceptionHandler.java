package com.zerobase.cms.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ExceptionResponse> customRequestException(
        final CustomException exception) {
        log.warn("api Exception : {}", exception.getErrorCode());
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(exception.getMessage(), exception.getErrorCode()));

    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class ExceptionResponse {

        private String message;
        private ErrorCode errorCode;
    }

}
