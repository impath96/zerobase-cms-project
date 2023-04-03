package com.zerobase.cms.user.service;

import com.zerobase.cms.user.dto.SendMailForm;

public interface EmailSendService {

    String sendEmail(SendMailForm form);
}
