package com.zerobase.cms.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.dto.SignUpForm;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    @DisplayName("이미 존재하는 이메일 인지 확인 - 존재하지 않을 경우")
    void isEmailExist() {

        String email = "test@naver.com";

        boolean exist = signUpCustomerService.isEmailExist(email);

        assertFalse(exist);

    }


}