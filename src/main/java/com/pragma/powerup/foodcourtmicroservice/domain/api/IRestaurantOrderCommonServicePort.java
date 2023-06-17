package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantOrderCommonServicePort {
    Restaurant findRestaurantById(Long id);
    List<OrderDurationInfoDto> getDurationOfFinalizedOrdersByRestaurant(Restaurant restaurant, Integer page, Integer sizePage);
}
