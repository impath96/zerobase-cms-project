package com.zerobase.cms.user.client;

import com.zerobase.cms.user.dto.SendMailForm;
import com.zerobase.cms.user.configuration.FeignConfiguration;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

// 모두 동일하게 적용이 되어야 하는 Contract 와 LocalDate, LocalTime, LocalDateTime 를
// ISO format 으로 보내는 설정정도는 모든 client 에 적용이 될 만한 것들
// 즉, Configuration 클래스 위에 @Configuration 어노테이션 붙이기

// 그게 아니면 각 FeignClient 에 적합한 Configuration 을 configuration = ???.class 설정하고
// 해당 Configuration 에는 @Configuration 어노테이션을 붙이지 않아야 한다.
@FeignClient(value = "mailgun", url = "https://api.mailgun.net/v3/", configuration = FeignConfiguration.class)
public interface MailgunClient {

    @PostMapping("sandbox2257c0b6f5f54673974e3d5a15dd0675.mailgun.org/messages")
    Response sendEmail(@SpringQueryMap SendMailForm form);

}
