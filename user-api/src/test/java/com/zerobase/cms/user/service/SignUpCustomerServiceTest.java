package com.zerobase.cms.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zerobase.cms.user.dto.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    private SignUpCustomerService signUpCustomerService;

    @Test
    @DisplayName("회원 가입 성공")
    void signUpSuccess() {
        // given
        SignUpForm form = SignUpForm.builder()
            .name("name")
            .birth(LocalDate.now())
            .email("test@naver.com")
            .password("1")
            .phone("010-1111-1111")
            .build();

        // when
        Customer customer = signUpCustomerService.signUp(form);

        // then
        // 회원 ID, 생성 날짜, 이름으로 확인
        assertNotNull(customer.getId());
        assertNotNull(customer.getCreatedAt());
        assertEquals(customer.getName(), form.getName());
    }

}