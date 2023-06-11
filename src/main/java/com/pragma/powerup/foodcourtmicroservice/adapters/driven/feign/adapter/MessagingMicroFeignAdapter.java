package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client.MessagingFeignClient;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.dto.request.SmsInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IMessagingCommunicationPort;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MessagingMicroFeignAdapter implements IMessagingCommunicationPort {
    private  MessagingFeignClient messagingFeignClient;
    private static final Logger logger = LoggerFactory.getLogger(MessagingMicroFeignAdapter.class);

    @Override
    public boolean sendSms(String phone, String smsBody) {
        //return true if there was an error sending the message.
        try{
            messagingFeignClient.sendSms(new SmsInfoDto(phone,smsBody));
            return false;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return true;
        }
    }
}
