package com.zerobase.cms.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class SendMailForm {

    private String from;
    private String to;
    private String subject;
    private String text;
}
