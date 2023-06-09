package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.NewOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderAndStatusMessagingDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderWithDetailDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.HistoryOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;

import java.util.List;

public interface IOrderServicePort {
    Order saveOrder(NewOrderDto newOrderDto, String token);

    Order findById(Long idOrder);

    Boolean existActiveOrderOfClient(Long idClient, Integer status, Long idRestaurant);

    List<OrderWithDetailDto> findAllPagedOrdersByIdStatus(Long idRestaurant, Integer status, Integer page, Integer sizePage, String token);

    Order assignOrder(Long idOrder, String token);

    OrderAndStatusMessagingDto changeStatusToReady(Long idOrder, String token);

    Order changeStatusToDelivered(Long idOrder, String pin, String token);

    Order changeStatusToCancelled(Long idOrder, String token);

    HistoryOrderDto getHistoryOfOrder(Long idOrder, String token);

    List<OrderDurationInfoDto> getDurationOfFinalizedOrdersByRestaurant(Restaurant restaurant,Integer page, Integer sizePage);
}
