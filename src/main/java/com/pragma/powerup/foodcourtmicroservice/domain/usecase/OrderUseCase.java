package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndAmountDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.NewOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderWithDetailDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.ClientAlreadyHasAnActiveOrderException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.UserHasNoPermissionException;
import com.pragma.powerup.foodcourtmicroservice.domain.mappers.OrderMapper;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserValidationComunicationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.ArgumentValidations;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.PaginationValidations;

import java.util.List;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.*;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final ITokenValidationPort tokenValidationPort;
    private final IDishServicePort dishServicePort;
    private final IRestaurantServicePort restaurantServicePort;

    private final IOrderDishServicePort orderDishServicePort;
    
    private final IUserValidationComunicationPort userValidationComunicationPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, ITokenValidationPort tokenValidationPort, IDishServicePort dishServicePort, IRestaurantServicePort restaurantServicePort, IOrderDishServicePort orderDishServicePort, IUserValidationComunicationPort userValidationComunicationPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.tokenValidationPort = tokenValidationPort;
        this.dishServicePort = dishServicePort;
        this.restaurantServicePort = restaurantServicePort;
        this.orderDishServicePort = orderDishServicePort;
        this.userValidationComunicationPort = userValidationComunicationPort;
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
        ArgumentValidations.validateObject(idRestaurant, ID_RESTAURANT_STRING_VALUE);
        ArgumentValidations.validateObject(status,ORDER_STATUS_STRING_VALUE);
        return orderPersistencePort.existOrderOfClientWithDifferentStatus(idClient,status,idRestaurant);
    }

    @Override
    public List<OrderWithDetailDto> findAllPagedOrdersByIdStatus(Long idRestaurant, Integer status, Integer page, Integer sizePage, String token) {
        tokenValidationPort.verifyRoleInToken(token, EMPLOYEE_ROLE_NAME);
        PaginationValidations.validatePageAndSizePage(page,sizePage);
        ArgumentValidations.validateObject(idRestaurant, ID_RESTAURANT_STRING_VALUE);
        ArgumentValidations.validateObject(status,ORDER_STATUS_STRING_VALUE);
        Boolean isAnEmployeeOfTheRestaurant = userValidationComunicationPort.existsRelationWithUserAndIdRestaurant(idRestaurant);
        if(Boolean.FALSE.equals(isAnEmployeeOfTheRestaurant))
            throw new UserHasNoPermissionException("User who made the request is not an employee of the restaurant.");
        List<Order> ordersByRestaurantAndStatus = orderPersistencePort.getOrdersByRestaurantAndStatus(page, sizePage, idRestaurant, status);
        if (ordersByRestaurantAndStatus.isEmpty())
            throw new NoDataFoundException("No orders found");
        return ordersByRestaurantAndStatus.stream()
                .map(order -> new OrderWithDetailDto(order, orderDishServicePort.getAllOrderDishByOrder(order)))
                .toList();
    }


}
