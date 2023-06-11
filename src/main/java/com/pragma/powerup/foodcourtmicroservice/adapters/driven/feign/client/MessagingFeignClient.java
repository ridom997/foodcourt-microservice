package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.dto.request.SmsInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messaging-microservice", url = "localhost:8093")
public interface MessagingFeignClient {
    @PostMapping(value = "/sms")
    void sendSms(@RequestBody SmsInfoDto smsInfoDto);
}
