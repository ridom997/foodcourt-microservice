package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.TraceabilityOrderDto;

import java.util.List;

public interface ITraceabilityCommunicationPort {
     void saveOrderLog(OrderLogDto orderLogDto);
     List<TraceabilityOrderDto> getTraceabilityListOfOrder(Long idOrder);
}
