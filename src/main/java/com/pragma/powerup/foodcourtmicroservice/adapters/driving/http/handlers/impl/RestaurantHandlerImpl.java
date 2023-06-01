package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IRestaurantHandler;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.request.IRestaurantRequestMapper;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.jwt.JwtUtils;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantHandlerImpl implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;


    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        restaurantServicePort.saveRestaurant(restaurantRequestMapper.toRestaurant(restaurantRequestDto));
    }

    @Override
    public Boolean userIsTheRestaurantOwner(Long idRestaurant) {
        return restaurantServicePort.isTheRestaurantOwner(JwtUtils.getTokenFromRequestHeaders(), idRestaurant);
    }

    @Override
    public List<RestaurantResponseDto> findAllPaged(int page, int sizePage) {
        List<Restaurant> restaurants = restaurantServicePort.findAllPaged(page, sizePage,JwtUtils.getTokenFromRequestHeaders());
        return restaurants.stream()
                .map(restaurantRequestMapper::toRestaurantResponseDto)
                .toList();
    }
}
