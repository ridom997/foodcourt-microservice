package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderDishEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderDishId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderDishEntityRepository extends JpaRepository<OrderDishEntity, OrderDishId> {

}
