package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndAmountDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.NewOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.ClientAlreadyHasAnActiveOrderException;
import com.pragma.powerup.foodcourtmicroservice.domain.mappers.OrderMapper;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.ArgumentValidations;

import java.util.List;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.CLIENT_ROLE_NAME;
import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.DELIVERED_ORDER_STATUS_INT_VALUE;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final ITokenValidationPort tokenValidationPort;
    private final IDishServicePort dishServicePort;
    private final IRestaurantServicePort restaurantServicePort;

    private final IOrderDishServicePort orderDishServicePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, ITokenValidationPort tokenValidationPort, IDishServicePort dishServicePort, IRestaurantServicePort restaurantServicePort, IOrderDishServicePort orderDishServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.tokenValidationPort = tokenValidationPort;
        this.dishServicePort = dishServicePort;
        this.restaurantServicePort = restaurantServicePort;
        this.orderDishServicePort = orderDishServicePort;
    }

    @Override
    public Order saveOrder(NewOrderDto newOrderDto, String token) {
        Long idClient = tokenValidationPort.findIdUserFromToken(token);
        tokenValidationPort.verifyRoleInToken(token, CLIENT_ROLE_NAME);
        Restaurant restaurant = restaurantServicePort.findById(newOrderDto.getIdRestaurant());
        if (Boolean.TRUE.equals(orderPersistencePort.existOrderOfClientWithDifferentStatus(idClient, DELIVERED_ORDER_STATUS_INT_VALUE,restaurant.getId())))
            throw new ClientAlreadyHasAnActiveOrderException();
        List<DishAndAmountDto> dishes = dishServicePort.generateValidatedDishList(restaurant.getId(), newOrderDto.getDishes());
        Order savedOrder = orderPersistencePort.saveOrder(OrderMapper.mapToPendingOrder(restaurant, idClient)); // mapping the order with default idChef and status
        orderDishServicePort.saveListOrderDish(savedOrder, dishes);
        return savedOrder;
    }

    @Override
    public Boolean existActiveOrderOfClient(Long idClient, Integer status, Long idRestaurant) {
        ArgumentValidations.validateObject(idClient, "idClient");
        ArgumentValidations.validateObject(idRestaurant, "idRestaurant");
        ArgumentValidations.validateObject(status,"Order status");
        return orderPersistencePort.existOrderOfClientWithDifferentStatus(idClient,status,idRestaurant);
    }


}
