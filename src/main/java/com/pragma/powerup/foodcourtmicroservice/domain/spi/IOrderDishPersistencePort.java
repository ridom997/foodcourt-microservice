package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.OrderDish;

import java.util.List;

public interface IOrderDishPersistencePort {
    List<OrderDish> saveAll(List<OrderDish> listOrderDish);
}
