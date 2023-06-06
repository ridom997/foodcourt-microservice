package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IOrderEntityRepository extends JpaRepository<OrderEntity,Long> {

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.idClient = :idClient AND o.status != :status AND o.idRestaurant.id = :idRestaurant")
    Integer findNumberOfOrdersOfClientWithDifferentStatus(Long idClient, Integer status, Long idRestaurant);
}
