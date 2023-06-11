package com.pragma.powerup.foodcourtmicroservice.configuration;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignCloudConfiguration implements RequestInterceptor {

    @Value("${jwt.token.admin}")
    private String tokenAdmin;

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Bean
    Logger.Level feignLoggerLevel(){
        return  Logger.Level.FULL;
    }

    public String getBearerTokenHeader(){
        return httpServletRequest.getHeader(Constants.AUTHORIZATION_HEADER);
    }

    public String getAdminJwtTokenHeader(){
        return httpServletRequest.getHeader(Constants.AUTHORIZATION_HEADER);
    }

    @Override
    public void apply(RequestTemplate template) {
        if(template.url().endsWith("/get-basic-info")){
            template.header(Constants.AUTHORIZATION_HEADER, "Bearer " +  tokenAdmin); //set admin jwt token
        }else{
            template.header(Constants.AUTHORIZATION_HEADER, getBearerTokenHeader()); //set current jwt token
        }
    }
}
