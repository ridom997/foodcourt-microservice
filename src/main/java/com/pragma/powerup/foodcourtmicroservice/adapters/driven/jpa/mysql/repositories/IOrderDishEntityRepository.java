package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderDishEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderDishId;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderDishEntityRepository extends JpaRepository<OrderDishEntity, OrderDishId> {

    @Query("SELECT od FROM OrderDishEntity od WHERE od.idOrder = :orderEntity")
    List<OrderDishEntity> findAllByIdOrder(OrderEntity orderEntity);
}
