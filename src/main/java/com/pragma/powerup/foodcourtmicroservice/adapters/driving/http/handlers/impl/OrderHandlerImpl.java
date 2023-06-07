package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewOrderRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.request.IOrderRequestMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response.IOrderResponseMapper;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.jwt.JwtUtils;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class OrderHandlerImpl implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponseDto createOrder(NewOrderRequestDto newOrderRequestDto) {
        return orderResponseMapper.toOrderResponseDto(
                orderServicePort.saveOrder(orderRequestMapper.toDomainObject(newOrderRequestDto), JwtUtils.getTokenFromRequestHeaders())
        );
    }
}