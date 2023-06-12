package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IUserValidationServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.*;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.*;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IMessagingCommunicationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITraceabilityCommunicationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private ITokenValidationPort tokenValidationPort;
    @Mock
    private IDishServicePort dishServicePort;
    @Mock
    private IRestaurantServicePort restaurantServicePort;
    @Mock
    private IOrderDishServicePort orderDishServicePort;

    @Mock
    private ITraceabilityCommunicationPort traceabilityCommunicationPort;

    @Mock
    private IUserValidationServicePort userValidationServicePort;

    @Mock
    private IMessagingCommunicationPort messagingCommunicationPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private NewOrderDto newOrderDto;


    @BeforeEach
    void beforeEach() {
        this.newOrderDto = new NewOrderDto(1L, List.of(
                new IdDishAndAmountDto(1L, 2),
                new IdDishAndAmountDto(2L, 1))
        );
    }

    @Test
    void saveOrderTest_successfully() {
        String token = "valid_token";
        Long idClient = 10L;
        Order orderSaved = new Order();
        orderSaved.setId(1L);
        List<DishAndAmountDto> dishAndAmountDtoList = List.of(new DishAndAmountDto(), new DishAndAmountDto());
        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idClient);
        when(restaurantServicePort.findById(newOrderDto.getIdRestaurant())).thenReturn(new Restaurant(1L));
        when(orderPersistencePort.existOrderOfClientWithDifferentStatus(idClient, DELIVERED_ORDER_STATUS_INT_VALUE, 1L)).thenReturn(false);
        when(dishServicePort.generateValidatedDishList(1L, newOrderDto.getDishes())).thenReturn(dishAndAmountDtoList);
        when(orderPersistencePort.saveOrder(any(Order.class))).thenReturn(orderSaved);

        Order result = orderUseCase.saveOrder(newOrderDto, token);

        verify(tokenValidationPort, times(1)).findIdUserFromToken(token);
        verify(tokenValidationPort, times(1)).verifyRoleInToken(token, "ROLE_CLIENT");
        verify(restaurantServicePort, times(1)).findById(newOrderDto.getIdRestaurant());
        verify(orderPersistencePort, times(1)).existOrderOfClientWithDifferentStatus(idClient, DELIVERED_ORDER_STATUS_INT_VALUE, newOrderDto.getIdRestaurant());
        verify(orderDishServicePort).saveListOrderDish(orderSaved, dishAndAmountDtoList);
        assertEquals(orderSaved.getId(), result.getId());
    }

    @Test
    void saveOrderTest_whenClientAlreadyHasActiveOrderThenThrowException() {
        String token = "valid_token";
        Long idClient = 10L;
        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idClient);
        when(restaurantServicePort.findById(newOrderDto.getIdRestaurant())).thenReturn(new Restaurant(1L));
        when(orderPersistencePort.existOrderOfClientWithDifferentStatus(idClient, DELIVERED_ORDER_STATUS_INT_VALUE, 1L)).thenReturn(true);

        // Act and Assert
        assertThrows(ClientAlreadyHasAnActiveOrderException.class, () -> orderUseCase.saveOrder(newOrderDto, token));

        // Verify
        verify(tokenValidationPort, times(1)).findIdUserFromToken(token);
        verify(tokenValidationPort, times(1)).verifyRoleInToken(token, "ROLE_CLIENT");
        verify(restaurantServicePort, times(1)).findById(newOrderDto.getIdRestaurant());
        verify(orderPersistencePort, times(1)).existOrderOfClientWithDifferentStatus(idClient, DELIVERED_ORDER_STATUS_INT_VALUE, newOrderDto.getIdRestaurant());
        verifyNoMoreInteractions(tokenValidationPort, restaurantServicePort, orderPersistencePort, dishServicePort, orderDishServicePort);
    }

    @Test
    void existActiveOrderOfClientTest_whenIdClientIsNullThenThrowException() {
        Long idClient = null;
        Integer status = DELIVERED_ORDER_STATUS_INT_VALUE;
        Long idRestaurant = 1L;

        assertThrows(FailValidatingRequiredVariableException.class, () -> {
            orderUseCase.existActiveOrderOfClient(idClient, status, idRestaurant);
        });

        verifyNoInteractions(orderPersistencePort);
    }

    @Test
    void existActiveOrderOfClientTest_whenIdRestaurantIsNullThenThrowException() {
        Long idClient = 1L;
        Integer status = DELIVERED_ORDER_STATUS_INT_VALUE;
        Long idRestaurant = null;

        assertThrows(FailValidatingRequiredVariableException.class, () -> {
            orderUseCase.existActiveOrderOfClient(idClient, status, idRestaurant);
        });

        verifyNoInteractions(orderPersistencePort);
    }

    @Test
    void existActiveOrderOfClientTest_whenStatusIsNullThenThrowException() {
        Long idClient = 1L;
        Long idRestaurant = 1L;
        Integer status = null;

        assertThrows(FailValidatingRequiredVariableException.class, () -> {
            orderUseCase.existActiveOrderOfClient(idClient, status, idRestaurant);
        });

        verifyNoInteractions(orderPersistencePort);
    }

    @Test
    void existActiveOrderOfClientTest_whenOrderDoesNotExist() {
        Long idClient = 1L;
        Integer status = DELIVERED_ORDER_STATUS_INT_VALUE;
        Long idRestaurant = 1L;

        when(orderPersistencePort.existOrderOfClientWithDifferentStatus(idClient, status, idRestaurant)).thenReturn(false);

        Boolean result = orderUseCase.existActiveOrderOfClient(idClient, status, idRestaurant);

        verify(orderPersistencePort, times(1)).existOrderOfClientWithDifferentStatus(idClient, status, idRestaurant);
        assertEquals(false, result);
    }

    @Test
    void existActiveOrderOfClientTest_whenOrderExists() {
        Long idClient = 1L;
        Integer status = DELIVERED_ORDER_STATUS_INT_VALUE;
        Long idRestaurant = 1L;

        when(orderPersistencePort.existOrderOfClientWithDifferentStatus(idClient, status, idRestaurant)).thenReturn(true);

        Boolean result = orderUseCase.existActiveOrderOfClient(idClient, status, idRestaurant);

        assertEquals(true, result);
        verify(orderPersistencePort, times(1)).existOrderOfClientWithDifferentStatus(idClient, status, idRestaurant);
    }


    @Test
    void findAllPagedOrdersByIdStatus_whenUserIsNotEmployee() {
        Long idRestaurant = 1L;
        Integer status = 1;
        Integer page = 0;
        Integer sizePage = 10;
        String token = "token";
        Boolean isAnEmployeeOfTheRestaurant = false;
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(idRestaurant)).thenReturn(isAnEmployeeOfTheRestaurant);

        assertThrows(UserHasNoPermissionException.class,
                () -> orderUseCase.findAllPagedOrdersByIdStatus(idRestaurant, status, page, sizePage, token)
        );

        verify(tokenValidationPort, times(1)).verifyRoleInToken(token, "ROLE_EMPLOYEE");
        verify(userValidationServicePort, times(1)).existsRelationWithUserAndIdRestaurant(idRestaurant);
        verify(orderPersistencePort, never()).getOrdersByRestaurantAndStatus(page, sizePage, idRestaurant, status);
    }

    @Test
    void findAllPagedOrdersByIdStatus_whenNoOrdersWithGivenStatus() {
        Long idRestaurant = 1L;
        Integer status = 1;
        Integer page = 0;
        Integer sizePage = 10;
        String token = "token";
        when(orderPersistencePort.getOrdersByRestaurantAndStatus(page, sizePage, idRestaurant, status)).thenReturn(List.of());
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(idRestaurant)).thenReturn(true);

        NoDataFoundException exception = assertThrows(NoDataFoundException.class, () -> {
            orderUseCase.findAllPagedOrdersByIdStatus(idRestaurant, status, page, sizePage, token);
        });

        assertEquals("No orders found", exception.getMessage());
        verify(tokenValidationPort, times(1)).verifyRoleInToken(token, "ROLE_EMPLOYEE");
        verify(userValidationServicePort, times(1)).existsRelationWithUserAndIdRestaurant(idRestaurant);
        verify(orderPersistencePort, times(1)).getOrdersByRestaurantAndStatus(page, sizePage, idRestaurant, status);
    }

    @Test
    void findAllPagedOrdersByIdStatus_successfully() {
        Long idRestaurant = 1L;
        Integer status = 1;
        Integer page = 0;
        Integer sizePage = 10;
        String token = "token";
        List<Order> orderList = List.of(new Order());
        when(orderPersistencePort.getOrdersByRestaurantAndStatus(page, sizePage, idRestaurant, status)).thenReturn(orderList);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(idRestaurant)).thenReturn(true);


        List<OrderWithDetailDto> allPagedOrdersByIdStatus = orderUseCase.findAllPagedOrdersByIdStatus(idRestaurant, status, page, sizePage, token);


        verify(tokenValidationPort, times(1)).verifyRoleInToken(token, "ROLE_EMPLOYEE");
        verify(userValidationServicePort, times(1)).existsRelationWithUserAndIdRestaurant(idRestaurant);
        verify(orderPersistencePort, times(1)).getOrdersByRestaurantAndStatus(page, sizePage, idRestaurant, status);
    }

    @Test
    @DisplayName("Should assign the order to an employee when the order is not taken or activated")
    void assignOrder_successful() {
        Long idOrder = 1L;
        String token = "token";
        Order order = new Order();
        order.setStatus(1);
        order.setIdClient(2L);
        order.setRestaurant(new Restaurant());
        Order orderSaved = new Order();
        orderSaved.setStatus(2);
        orderSaved.setIdChef(1L);
        UserBasicInfoDto userBasicInfoDto = new UserBasicInfoDto(1L,"user name", "asdas", "mail");
        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(1L);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);
        when(userValidationServicePort.findClientAndEmployeeInfo(anyLong(),anyLong())).thenReturn(new OrderActorsDto(userBasicInfoDto,userBasicInfoDto));
        when(orderPersistencePort.saveOrderAndTraceability(any(Order.class),any(OrderLogDto.class))).thenReturn(orderSaved);

        Order orderResult = orderUseCase.assignOrder(idOrder, token);

        assertEquals(2,orderResult.getStatus());
    }


    @Test
    void assignOrder_WhenUserIsNotEmployeeOfRestaurantThenThrowException() {
        Long idOrder = 1L;
        String token = "token";
        Order order = new Order();
        order.setStatus(1);
        order.setRestaurant(new Restaurant());
        Order orderSaved = new Order();
        orderSaved.setStatus(2);
        orderSaved.setIdChef(1L);
        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(1L);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(false);

        assertThrows(UserHasNoPermissionException.class, () -> orderUseCase.assignOrder(idOrder, token));
    }

    @Test
    void assignOrder_whenOrderIsAlreadyTakenOrActivatedThenThrowException() {
        Long idOrder = 1L;
        String token = "token";
        Order order = new Order();
        order.setStatus(2);
        order.setRestaurant(new Restaurant());
        Order orderSaved = new Order();
        orderSaved.setStatus(2);
        orderSaved.setIdChef(1L);
        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(1L);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);

        assertThrows(UserHasNoPermissionException.class, () -> orderUseCase.assignOrder(idOrder, token));
    }

    @Test
    void findById_whenNullReturned() {
        Long idOrder = 1L;
        when(orderPersistencePort.findById(idOrder)).thenReturn(null);
        assertThrows(NoDataFoundException.class, () -> orderUseCase.findById(idOrder));
    }

    @Test
    void changeStatusToReadyTest_succesfully(){
        Long idOrder = 1L;
        String token = "valid_token";
        Long idEmployee = 1L;
        Long idClient = 2L;
        OrderActorsDto orderActorsDto = new OrderActorsDto();
        orderActorsDto.setClient(new UserBasicInfoDto(idClient,"a","b","c"));
        orderActorsDto.setEmployee(new UserBasicInfoDto(idEmployee,"a","b","c"));

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(IN_PROGRESS_ORDER_STATUS_INT_VALUE);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(idClient);
        order.setIdChef(1L);

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idEmployee);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);
        when(userValidationServicePort.findClientAndEmployeeInfo(idClient,idEmployee)).thenReturn(orderActorsDto);
        Order orderSaved = new Order();
        orderSaved.setStatus(READY_ORDER_STATUS_INT_VALUE);
        when(orderPersistencePort.saveOrderAndTraceability(any(Order.class), any(OrderLogDto.class))).thenReturn(orderSaved);
        when(messagingCommunicationPort.sendSms(anyString(),anyString())).thenReturn(false);

        //act
        OrderAndStatusMessagingDto result = orderUseCase.changeStatusToReady(idOrder, token);

        assertEquals(READY_ORDER_STATUS_INT_VALUE,result.getOrder().getStatus());
        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
        verify(userValidationServicePort).existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId());
        verify(userValidationServicePort).findClientAndEmployeeInfo(idClient, idEmployee);
        verify(orderPersistencePort).saveOrderAndTraceability(any(Order.class), any(OrderLogDto.class));
        verify(messagingCommunicationPort).sendSms(anyString(), anyString());

    }

    @Test
    void changeStatusToReadyTest_isNotAnEmployeeOfTheRestaurant(){
        Long idOrder = 1L;
        String token = "valid_token";
        Long idEmployee = 1L;
        Long idClient = 2L;
        OrderActorsDto orderActorsDto = new OrderActorsDto();
        orderActorsDto.setClient(new UserBasicInfoDto(idClient,"a","b","c"));
        orderActorsDto.setEmployee(new UserBasicInfoDto(idEmployee,"a","b","c"));

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(IN_PROGRESS_ORDER_STATUS_INT_VALUE);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(idClient);

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idEmployee);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(false);


        //act
        assertThrows(UserHasNoPermissionException.class ,() -> orderUseCase.changeStatusToReady(idOrder, token));

        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
        verify(userValidationServicePort).existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId());
        verify(userValidationServicePort, times(0)).findClientAndEmployeeInfo(idClient, idEmployee);
        verify(orderPersistencePort, times(0)).saveOrderAndTraceability(any(Order.class), any(OrderLogDto.class));
        verify(messagingCommunicationPort, times(0)).sendSms(anyString(), anyString());

    }

    @Test
    void changeStatusToReadyTest_orderIsNotInProgress(){
        Long idOrder = 1L;
        String token = "valid_token";
        Long idEmployee = 1L;
        Long idClient = 2L;
        OrderActorsDto orderActorsDto = new OrderActorsDto();
        orderActorsDto.setClient(new UserBasicInfoDto(idClient,"a","b","c"));
        orderActorsDto.setEmployee(new UserBasicInfoDto(idEmployee,"a","b","c"));

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(1);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(idClient);

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idEmployee);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);

        //act
        assertThrows(UserHasNoPermissionException.class ,() -> orderUseCase.changeStatusToReady(idOrder, token));

        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
        verify(userValidationServicePort).existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId());
        verify(userValidationServicePort, times(0)).findClientAndEmployeeInfo(idClient, idEmployee);
        verify(orderPersistencePort, times(0)).saveOrderAndTraceability(any(Order.class), any(OrderLogDto.class));
        verify(messagingCommunicationPort, times(0)).sendSms(anyString(), anyString());
    }


    @Test
    void changeStatusToDelivered_successfully(){
        Long idOrder = 1L;
        String token = "valid_token";
        String pin = "aadd";
        Long idEmployee = 1L;
        Long idClient = 2L;
        OrderActorsDto orderActorsDto = new OrderActorsDto();
        orderActorsDto.setClient(new UserBasicInfoDto(idClient,"a","b","c"));
        orderActorsDto.setEmployee(new UserBasicInfoDto(idEmployee,"a","b","c"));

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(READY_ORDER_STATUS_INT_VALUE);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(idClient);
        order.setIdChef(1L);
        order.setDeliveryPin(pin);

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idEmployee);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);
        when(userValidationServicePort.findClientAndEmployeeInfo(idClient,idEmployee)).thenReturn(orderActorsDto);
        Order orderSaved = new Order();
        orderSaved.setStatus(DELIVERED_ORDER_STATUS_INT_VALUE);
        when(orderPersistencePort.saveOrderAndTraceability(any(Order.class), any(OrderLogDto.class))).thenReturn(orderSaved);

        //act
        Order result = orderUseCase.changeStatusToDelivered(idOrder, pin, token);

        assertEquals(DELIVERED_ORDER_STATUS_INT_VALUE,result.getStatus());
        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
        verify(userValidationServicePort).existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId());
        verify(userValidationServicePort).findClientAndEmployeeInfo(idClient, idEmployee);
        verify(orderPersistencePort).saveOrderAndTraceability(any(Order.class), any(OrderLogDto.class));
    }

    @Test
    void changeStatusToDelivered_userHasNoPermissionException(){
        Long idOrder = 1L;
        String token = "valid_token";
        String pin = "aadd";
        Long idEmployee = 1L;
        Long idClient = 2L;
        OrderActorsDto orderActorsDto = new OrderActorsDto();
        orderActorsDto.setClient(new UserBasicInfoDto(idClient,"a","b","c"));
        orderActorsDto.setEmployee(new UserBasicInfoDto(idEmployee,"a","b","c"));

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(READY_ORDER_STATUS_INT_VALUE);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(idClient);
        order.setIdChef(30L);
        order.setDeliveryPin(pin);

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idEmployee);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);

        //act
        assertThrows(UserHasNoPermissionException.class, () -> orderUseCase.changeStatusToDelivered(idOrder, pin, token));

        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
        verify(userValidationServicePort).existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId());
    }

    @Test
    void changeStatusToDelivered_givenPinIsNotCorrectException(){
        Long idOrder = 1L;
        String token = "valid_token";
        String pin = "aadd";
        Long idEmployee = 1L;
        Long idClient = 2L;
        OrderActorsDto orderActorsDto = new OrderActorsDto();
        orderActorsDto.setClient(new UserBasicInfoDto(idClient,"a","b","c"));
        orderActorsDto.setEmployee(new UserBasicInfoDto(idEmployee,"a","b","c"));

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(READY_ORDER_STATUS_INT_VALUE);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(idClient);
        order.setIdChef(1L);
        order.setDeliveryPin("DIFFERENT_PIN");

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idEmployee);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);


        //act
        assertThrows(GivenPinIsNotCorrectException.class, () -> orderUseCase.changeStatusToDelivered(idOrder, pin, token));

        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
        verify(userValidationServicePort).existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId());
    }

    @Test
    void changeStatusToCancelled_successfully(){
        Long idOrder = 1L;
        String token = "valid_token";
        Long idClient = 2L;
        UserBasicInfoDto clientBasicInfo = new UserBasicInfoDto(idClient, "a", "b", "c");

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(PENDING_ORDER_STATUS_INT_VALUE);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(idClient);

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idClient);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);
        when(userValidationServicePort.findClientInfo(idClient)).thenReturn(clientBasicInfo);
        Order orderSaved = new Order();
        orderSaved.setStatus(CANCELLED_ORDER_STATUS_INT_VALUE);
        when(orderPersistencePort.saveOrderAndTraceability(any(Order.class), any(OrderLogDto.class))).thenReturn(orderSaved);

        //act
        Order result = orderUseCase.changeStatusToCancelled(idOrder, token);

        assertEquals(CANCELLED_ORDER_STATUS_INT_VALUE,result.getStatus());
        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
        verify(userValidationServicePort).findClientInfo(idClient);
        verify(orderPersistencePort).saveOrderAndTraceability(any(Order.class), any(OrderLogDto.class));
    }

    @Test
    void changeStatusToCancelled_orderIsNotOfClient(){
        Long idOrder = 1L;
        String token = "valid_token";
        Long idClient = 2L;

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(PENDING_ORDER_STATUS_INT_VALUE);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(3L);

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idClient);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);

        //act
        assertThrows(UserHasNoPermissionException.class, () -> orderUseCase.changeStatusToCancelled(idOrder, token));

        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
    }

    @Test
    void changeStatusToCancelled_orderIsNotPending(){
        Long idOrder = 1L;
        String token = "valid_token";
        Long idClient = 2L;

        Order order = new Order();
        order.setId(idOrder);
        order.setStatus(IN_PROGRESS_ORDER_STATUS_INT_VALUE);
        order.setRestaurant(new Restaurant(1L));
        order.setIdClient(idClient);

        when(tokenValidationPort.findIdUserFromToken(token)).thenReturn(idClient);
        when(orderPersistencePort.findById(idOrder)).thenReturn(order);

        //act
        assertThrows(UserHasNoPermissionException.class, () -> orderUseCase.changeStatusToCancelled(idOrder, token));

        verify(tokenValidationPort).findIdUserFromToken(token);
        verify(orderPersistencePort).findById(idOrder);
    }
}