package com.pragma.powerup.foodcourtmicroservice.domain.spi;

public interface IMessagingCommunicationPort {
    boolean sendSms(String phone, String smsBody);
}
