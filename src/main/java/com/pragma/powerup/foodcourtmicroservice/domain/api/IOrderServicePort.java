package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.NewOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderAndStatusMessagingDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderWithDetailDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;

import java.util.List;

public interface IOrderServicePort {
    Order saveOrder(NewOrderDto newOrderDto, String token);

    Order findById(Long idOrder);

    Boolean existActiveOrderOfClient(Long idClient, Integer status, Long idRestaurant);

    List<OrderWithDetailDto> findAllPagedOrdersByIdStatus(Long idRestaurant, Integer status, Integer page, Integer sizePage, String token);

    Order assignOrder(Long idOrder, String token);

    OrderAndStatusMessagingDto changeStatusToReady(Long idOrder, String token);

}
