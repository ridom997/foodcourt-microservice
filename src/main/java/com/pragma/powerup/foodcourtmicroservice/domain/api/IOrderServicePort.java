package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.NewOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;

public interface IOrderServicePort {
    Order saveOrder(NewOrderDto newOrderDto, String token);

    Boolean existActiveOrderOfClient(Long idClient, Integer status, Long idRestaurant);

}
