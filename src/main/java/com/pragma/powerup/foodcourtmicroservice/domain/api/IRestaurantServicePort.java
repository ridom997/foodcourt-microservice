package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    Restaurant findById(Long id);
    Boolean isTheRestaurantOwner(Long idUser, Restaurant restaurant);
    Boolean isTheRestaurantOwner(String token, Long idRestaurant);
    Boolean isTheRestaurantOwner(String token, Restaurant restaurant);

    List<Restaurant> findAllPaged(Integer page,Integer sizePage, String token);

    List<OrderDurationInfoDto> getDurationOfOrdersByRestaurant(Long idRestaurant, Integer page, Integer sizePage, String token);
}
