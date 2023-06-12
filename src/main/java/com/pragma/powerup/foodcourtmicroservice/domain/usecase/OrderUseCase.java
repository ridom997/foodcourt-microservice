package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.*;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.*;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.ClientAlreadyHasAnActiveOrderException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.UserHasNoPermissionException;
import com.pragma.powerup.foodcourtmicroservice.domain.mappers.OrderMapper;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IMessagingCommunicationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.utils.OrderUtils;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.ArgumentValidations;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.PaginationValidations;

import java.util.List;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.*;
import static com.pragma.powerup.foodcourtmicroservice.domain.constants.MessageConstants.USER_IS_NOT_AN_EMPLOYEE_OF_THE_RESTAURANT;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final ITokenValidationPort tokenValidationPort;
    private final IDishServicePort dishServicePort;
    private final IRestaurantServicePort restaurantServicePort;

    private final IOrderDishServicePort orderDishServicePort;
    private final IMessagingCommunicationPort messagingCommunicationPort;
    
    private final IUserValidationServicePort userValidationServicePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, ITokenValidationPort tokenValidationPort, IDishServicePort dishServicePort, IRestaurantServicePort restaurantServicePort, IOrderDishServicePort orderDishServicePort, IMessagingCommunicationPort messagingCommunicationPort, IUserValidationServicePort userValidationServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.tokenValidationPort = tokenValidationPort;
        this.dishServicePort = dishServicePort;
        this.restaurantServicePort = restaurantServicePort;
        this.orderDishServicePort = orderDishServicePort;
        this.messagingCommunicationPort = messagingCommunicationPort;
        this.userValidationServicePort = userValidationServicePort;
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
    public Order findById(Long idOrder) {
        ArgumentValidations.validateObject(idOrder, "idOrder");
        Order order = orderPersistencePort.findById(idOrder);
        if (order == null)
            throw new NoDataFoundException("Order not found");
        return order;
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
        Boolean isAnEmployeeOfTheRestaurant = userValidationServicePort.existsRelationWithUserAndIdRestaurant(idRestaurant);
        if(Boolean.FALSE.equals(isAnEmployeeOfTheRestaurant))
            throw new UserHasNoPermissionException(USER_IS_NOT_AN_EMPLOYEE_OF_THE_RESTAURANT);
        List<Order> ordersByRestaurantAndStatus = orderPersistencePort.getOrdersByRestaurantAndStatus(page, sizePage, idRestaurant, status);
        if (ordersByRestaurantAndStatus.isEmpty())
            throw new NoDataFoundException("No orders found");
        return ordersByRestaurantAndStatus.stream()
                .map(order -> new OrderWithDetailDto(order, orderDishServicePort.getAllOrderDishByOrder(order)))
                .toList();
    }

    @Override
    public Order assignOrder(Long idOrder, String token) {
        Long idEmployee = validateRoleAndReturnIdUserFromToken(token,EMPLOYEE_ROLE_NAME);
        Order order = findById(idOrder);
        Boolean isAnEmployeeOfTheRestaurant = userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId());
        if(Boolean.FALSE.equals(isAnEmployeeOfTheRestaurant))
            throw new UserHasNoPermissionException(USER_IS_NOT_AN_EMPLOYEE_OF_THE_RESTAURANT);
        if (order.getStatus() != 1 ||  (order.getIdChef() != null && order.getIdChef() != 0))
            throw new UserHasNoPermissionException("Order is already taken or activated");
        OrderActorsDto clientAndEmployeeInfo = userValidationServicePort.findClientAndEmployeeInfo(order.getIdClient(), idEmployee);
        order.setIdChef(idEmployee);
        order.setStatus(IN_PROGRESS_ORDER_STATUS_INT_VALUE);
        return orderPersistencePort.saveOrderAndTraceability(order,OrderMapper.mapToOrderLogDto(order,PENDING_ORDER_STATUS_INT_VALUE,clientAndEmployeeInfo));
    }

    @Override
    public OrderAndStatusMessagingDto changeStatusToReady(Long idOrder, String token) {
        Long idEmployee = validateRoleAndReturnIdUserFromToken(token,EMPLOYEE_ROLE_NAME);
        Order order = findById(idOrder);
        Boolean isAnEmployeeOfTheRestaurant = userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId());
        if(Boolean.FALSE.equals(isAnEmployeeOfTheRestaurant))
            throw new UserHasNoPermissionException(USER_IS_NOT_AN_EMPLOYEE_OF_THE_RESTAURANT);
        if(!order.getStatus().equals(IN_PROGRESS_ORDER_STATUS_INT_VALUE) || !order.getIdChef().equals(idEmployee))
            throw new UserHasNoPermissionException("Order is not in progress or it doesn't belong to the employee");
        OrderActorsDto clientAndEmployeeInfo = userValidationServicePort.findClientAndEmployeeInfo(order.getIdClient(), idEmployee);
        order.setStatus(READY_ORDER_STATUS_INT_VALUE);
        String deliveryPin = OrderUtils.generateDeliveryPin();
        order.setDeliveryPin(deliveryPin);
        return new OrderAndStatusMessagingDto(
                orderPersistencePort.saveOrderAndTraceability(order, OrderMapper.mapToOrderLogDto(order,IN_PROGRESS_ORDER_STATUS_INT_VALUE,clientAndEmployeeInfo)),
                messagingCommunicationPort.sendSms(clientAndEmployeeInfo.getClient().getPhone(), ORDER_READY_SMS_BODY_BASE_MESSAGE+deliveryPin)
        );
    }

    private Long validateRoleAndReturnIdUserFromToken(String token, String role){
        Long idEmployee = tokenValidationPort.findIdUserFromToken(token);
        tokenValidationPort.verifyRoleInToken(token, role);
        return idEmployee;
    }
}
