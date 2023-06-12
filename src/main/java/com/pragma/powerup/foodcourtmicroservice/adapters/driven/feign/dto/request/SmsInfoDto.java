package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SmsInfoDto {
    private String phone;
    private String smsBody;
}
