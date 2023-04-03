package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.mailgun.SendMailForm;

public interface EmailSendService {

    String sendEmail(String to);

    String sendEmail(SendMailForm form);
}
