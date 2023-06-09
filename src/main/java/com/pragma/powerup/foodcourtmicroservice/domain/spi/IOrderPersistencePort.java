package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.EmployeePerformanceDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;

import java.util.List;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    Order saveOrderAndTraceability(Order order, OrderLogDto orderLogDto);
    Order findById(Long idOrder);
    Boolean existOrderOfClientWithDifferentStatus(Long idClient, Integer status, Long idRestaurant);
    List<Order> getOrdersByRestaurantAndStatus(Integer page, Integer sizePage, Long idRestaurant, Integer status);

    List<OrderDurationInfoDto> findAllPagedCompletedOrdersByIdRestaurant(Long idRestaurant, Integer page, Integer sizePage);
    List<EmployeePerformanceDto> getRankingOfEmployeesByRestaurant(Long idRestaurant, Integer page, Integer sizePage);

}
