package com.zerobase.cms.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zerobase.cms.user.dto.SendMailForm;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

    // 지금은 MailgunEmailSendService 만 빈으로 등록되어 있어서 상관 없지만
    // 만약 다른 EmailSendService 를 구현하게 될 경우 각 구현체 별로 테스트를 진행
    @Autowired
    private EmailSendService emailSendService;

    @Test
    @DisplayName("mailgun을 이용한 이메일 전송 - 성공")
    public void sendEmailSuccess() {

        String to = "impath96@gmail.com";
        SendMailForm sendMailForm = SendMailForm.builder()
            .from("test-account@naver.com")
            .to(to)
            .subject("Verification Email")
            .text("test")
            .build();
        String response = emailSendService.sendEmail(sendMailForm);

        assertNotNull(response);
        System.out.println(response);


    }

    @Test
    @DisplayName("잘못된 이메일 형식 - mailgun을 이용한 이메일 전송 실패")
    public void sendEmailFail_WRONG_EMAIL() {

        String to = "impath96gmail.com";
        SendMailForm sendMailForm = SendMailForm.builder()
            .from("test-account@naver.com")
            .to(to)
            .subject("Verification Email")
            .text("test")
            .build();

        CustomException customException = assertThrows(CustomException.class,
            () -> emailSendService.sendEmail(sendMailForm));

        assertEquals(ErrorCode.WRONG_EMAIL, customException.getErrorCode());

    }

}