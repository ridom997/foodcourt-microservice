package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;

public interface ITraceabilityCommunicationPort {
     void saveOrderLog(OrderLogDto orderLogDto);
}
