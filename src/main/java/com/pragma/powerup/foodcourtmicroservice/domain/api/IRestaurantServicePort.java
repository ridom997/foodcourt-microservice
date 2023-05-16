package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    Restaurant findById(Long id);
    Boolean isTheRestaurantOwner(Long idUser, Restaurant restaurant);
}
