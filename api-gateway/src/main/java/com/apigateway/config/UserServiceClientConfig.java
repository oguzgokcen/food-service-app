package com.apigateway.config;

import feign.Request;
import org.springframework.context.annotation.Bean;

public class UserServiceClientConfig {
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(60000, 60000); // connectTimeout, readTimeout
    }
}
