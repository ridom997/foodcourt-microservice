package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderEntityRepository extends JpaRepository<OrderEntity,Long> {

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.idClient = :idClient AND o.status != :status AND o.idRestaurant.id = :idRestaurant")
    Integer findNumberOfOrdersOfClientWithDifferentStatus(Long idClient, Integer status, Long idRestaurant);

    @Query("SELECT o FROM OrderEntity o WHERE o.status = :status AND o.idRestaurant.id = :idRestaurant")
    Page<OrderEntity> getOrdersByIdRestaurantAndStatus(Long idRestaurant, Integer status, Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE (o.status = 5 OR o.status = 4) AND o.idRestaurant.id = :idRestaurant")
    Page<OrderEntity> getCompletedOrdersByIdRestaurant(Long idRestaurant, Pageable pageable);

    @Query(value = "SELECT o.idChef as idEmployee, AVG(o.dateFinished - o.date) as averageTime, COUNT(o.id) as numberOfOrders " +
            "FROM OrderEntity o " +
            "WHERE o.idRestaurant.id = :idRestaurant AND o.status = 4 " +
            "GROUP BY o.idChef " +
            "ORDER BY averageTime ASC")
    List<Tuple> getRankingOfEmployeesByRestaurant(Long idRestaurant, Pageable pageable);
}
