package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;

import java.util.List;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    Boolean existOrderOfClientWithDifferentStatus(Long idClient, Integer status, Long idRestaurant);
    List<Order> getOrdersByRestaurantAndStatus(Integer page, Integer sizePage, Long idRestaurant, Integer status);
}
