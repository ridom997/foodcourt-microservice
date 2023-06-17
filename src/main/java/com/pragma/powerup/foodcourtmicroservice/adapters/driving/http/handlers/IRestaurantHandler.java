package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderDurationInfoResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantResponseDto;

import java.util.List;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    Boolean userIsTheRestaurantOwner(Long idRestaurant);

    List<RestaurantResponseDto> findAllPaged(int page, int sizePage);

    List<OrderDurationInfoResponseDto> getDurationOfOrdersByRestaurant(Long idRestaurant, Integer page, Integer sizePage);
}
