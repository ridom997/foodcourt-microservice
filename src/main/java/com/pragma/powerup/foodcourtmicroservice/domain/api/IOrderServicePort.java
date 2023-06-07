package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.NewOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderWithDetailDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;

import java.util.List;

public interface IOrderServicePort {
    Order saveOrder(NewOrderDto newOrderDto, String token);

    Boolean existActiveOrderOfClient(Long idClient, Integer status, Long idRestaurant);

    List<OrderWithDetailDto> findAllPagedOrdersByIdStatus(Long idRestaurant, Integer status, Integer page, Integer sizePage, String token);

}