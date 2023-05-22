package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IRestaurantHandler;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.request.IRestaurantRequestMapper;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RestaurantHandlerImpl implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;


    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        restaurantServicePort.saveRestaurant(restaurantRequestMapper.toRestaurant(restaurantRequestDto));
    }
}
