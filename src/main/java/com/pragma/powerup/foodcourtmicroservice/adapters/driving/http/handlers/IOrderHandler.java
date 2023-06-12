package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewOrderRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderAndStatusMessagingResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderWithDetailResponseDto;

import java.util.List;

public interface IOrderHandler {
    OrderResponseDto createOrder(NewOrderRequestDto newOrderRequestDto);
    List<OrderWithDetailResponseDto> findAllPagedOrdersByIdStatus(Long idRestaurant, Integer status, Integer page, Integer sizePage);

    OrderResponseDto assignOrder(Long idOrder);
    OrderAndStatusMessagingResponseDto orderReady(Long idOrder);

    OrderResponseDto orderDelivered(Long idOrder, String pin);
}
