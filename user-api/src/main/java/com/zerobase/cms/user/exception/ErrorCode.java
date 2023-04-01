package com.zerobase.cms.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // mailgun feign 관련 에러
    WRONG_EMAIL(HttpStatus.BAD_REQUEST, "잘못된 이메일 형식입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
