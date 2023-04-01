package com.zerobase.cms.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// 나중에 필드 Mocking 으로 테스트를 진행해보자
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    private String email;
    private String name;
    private String password;
    private LocalDate birth;
    private String phone;

}
