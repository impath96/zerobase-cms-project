package com.zerobase.cms.user.configuration;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/mailgun.properties")
public class FeignConfiguration {

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {

        return new BasicAuthRequestInterceptor("api", apiKey);

    }

}
