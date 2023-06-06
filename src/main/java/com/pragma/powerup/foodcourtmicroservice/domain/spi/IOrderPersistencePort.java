package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    Boolean existOrderOfClientWithDifferentStatus(Long idClient, Integer status, Long idRestaurant);
}
