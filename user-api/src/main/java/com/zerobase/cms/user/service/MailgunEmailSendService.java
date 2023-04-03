package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.FeignResponseUtils;
import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.dto.SendMailForm;
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
public class MailgunEmailSendService implements EmailSendService {

    private final MailgunClient mailgunClient;

    @Override
    public String sendEmail(SendMailForm form) {

        Response response = mailgunClient.sendEmail(form);

        if (!isOk(response)) {
            throw new CustomException(ErrorCode.WRONG_EMAIL);
        }

        return FeignResponseUtils.getResponseBody(response);
    }

    private boolean isOk(Response response) {
        return response.status() == HttpStatus.OK.value();
    }
}
