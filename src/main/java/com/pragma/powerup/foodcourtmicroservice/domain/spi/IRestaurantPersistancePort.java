package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;

public interface IRestaurantPersistancePort {
    void saveRestaurant(Restaurant restaurant);
}
