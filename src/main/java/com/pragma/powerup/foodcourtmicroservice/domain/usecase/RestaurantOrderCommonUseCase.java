package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantOrderCommonServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoRestaurantFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.ArgumentValidations;

import java.util.List;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.ID_RESTAURANT_STRING_VALUE;
import static com.pragma.powerup.foodcourtmicroservice.domain.constants.MessageConstants.NO_ORDERS_FOUND_MESSAGE;

public class RestaurantOrderCommonUseCase implements IRestaurantOrderCommonServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;

    public RestaurantOrderCommonUseCase(IRestaurantPersistencePort restaurantPersistencePort, IOrderPersistencePort orderPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
    }


    @Override
    public Restaurant findRestaurantById(Long id) {
        ArgumentValidations.validateObject(id, ID_RESTAURANT_STRING_VALUE);
        Restaurant restaurant = restaurantPersistencePort.findById(id);
        if (restaurant == null)
            throw new NoRestaurantFoundException();
        return restaurant;
    }

    @Override
    public List<OrderDurationInfoDto> getDurationOfFinalizedOrdersByRestaurant(Restaurant restaurant, Integer page, Integer sizePage) {
        List<OrderDurationInfoDto> allPagedCompletedOrdersByIdRestaurant = orderPersistencePort.findAllPagedCompletedOrdersByIdRestaurant(restaurant.getId(), page, sizePage);
        if (allPagedCompletedOrdersByIdRestaurant.isEmpty())
            throw new NoDataFoundException(NO_ORDERS_FOUND_MESSAGE);
        return allPagedCompletedOrdersByIdRestaurant;
    }
}
