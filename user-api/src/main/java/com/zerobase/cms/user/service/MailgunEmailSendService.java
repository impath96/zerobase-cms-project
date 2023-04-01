package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.FeignResponseUtils;
import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:/mailgun.properties")
public class MailgunEmailSendService implements EmailSendService{

    private final MailgunClient mailgunClient;

    @Override
    public String sendEmail(String to) {
        // 나중에 이메일 전송을 위한 템플릿을 따로 구현
        SendMailForm form = SendMailForm.builder()
                .from("aksghcjswo@naver.com")
                .to(to)
                .subject("test email from zerobase!!!@@@$!$@$@#")
                .text("text!!")
                .build();
        Response response = mailgunClient.sendEmail(form);

        if(response.status() != HttpStatus.OK.value()) {
            throw new CustomException(ErrorCode.WRONG_EMAIL);
        }

        return FeignResponseUtils.getResponseBody(response);
    }
}
