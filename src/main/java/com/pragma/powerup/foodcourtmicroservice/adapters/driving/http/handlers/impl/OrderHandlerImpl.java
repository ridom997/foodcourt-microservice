package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewOrderRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderAndStatusMessagingResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderWithDetailResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.request.IOrderRequestMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response.IOrderResponseMapper;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.jwt.JwtUtils;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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

    @Override
    public List<OrderWithDetailResponseDto> findAllPagedOrdersByIdStatus(Long idRestaurant, Integer status, Integer page, Integer sizePage) {
        return orderServicePort.findAllPagedOrdersByIdStatus(idRestaurant,status,page,sizePage,JwtUtils.getTokenFromRequestHeaders())
                .stream()
                .map(orderResponseMapper::toOrderWithDetailResponseDto).toList();
    }

    @Override
    public OrderResponseDto assignOrder(Long idOrder) {
        return orderResponseMapper.toOrderResponseDto(
                orderServicePort.assignOrder(idOrder, JwtUtils.getTokenFromRequestHeaders())
        );
    }

    @Override
    public OrderAndStatusMessagingResponseDto orderReady(Long idOrder) {
        OrderAndStatusMessagingResponseDto responseDto = orderResponseMapper.toOrderAndStatusMessagingResponseDto(
                orderServicePort.changeStatusToReady(idOrder, JwtUtils.getTokenFromRequestHeaders()));
        if(Boolean.FALSE.equals(responseDto.getErrorSendingSms())) //if there was an error sending the sms then we return the PIN in the response
            responseDto.getOrder().setDeliveryPin(null);
        return responseDto;
    }
}
