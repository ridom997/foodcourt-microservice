package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistancePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestaurantMysqlAdapter implements IRestaurantPersistancePort {

    private final IRestaurantEntityRepository restaurantEntityRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantEntityRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }
}
