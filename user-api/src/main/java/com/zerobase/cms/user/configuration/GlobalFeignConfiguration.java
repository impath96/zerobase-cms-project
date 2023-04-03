package com.zerobase.cms.user.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.zerobase.cms.user")
public class GlobalFeignConfiguration {

}
