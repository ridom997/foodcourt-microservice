package com.pragma.powerup.foodcourtmicroservice.configuration;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FeignCloudConfiguration implements RequestInterceptor {

    private HttpServletRequest httpServletRequest;
    @Bean
    Logger.Level feignLoggerLevel(){
        return  Logger.Level.FULL;
    }

    public String getBearerTokenHeader(){
        return httpServletRequest.getHeader(Constants.AUTHORIZATION_HEADER);
    }
    @Override
    public void apply(RequestTemplate template) {
        template.header(Constants.AUTHORIZATION_HEADER, getBearerTokenHeader());
    }
}
