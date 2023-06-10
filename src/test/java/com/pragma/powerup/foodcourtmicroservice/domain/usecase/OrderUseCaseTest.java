package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.*;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.ClientAlreadyHasAnActiveOrderException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.UserHasNoPermissionException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITraceabilityCommunicationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserValidationComunicationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.DELIVERED_ORDER_STATUS_INT_VALUE;
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
    private IUserValidationComunicationPort userValidationComunicationPort;

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
        when(userValidationComunicationPort.existsRelationWithUserAndIdRestaurant(idRestaurant)).thenReturn(isAnEmployeeOfTheRestaurant);

        assertThrows(UserHasNoPermissionException.class,
                () -> orderUseCase.findAllPagedOrdersByIdStatus(idRestaurant, status, page, sizePage, token)
        );

        verify(tokenValidationPort, times(1)).verifyRoleInToken(token, "ROLE_EMPLOYEE");
        verify(userValidationComunicationPort, times(1)).existsRelationWithUserAndIdRestaurant(idRestaurant);
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
        when(userValidationComunicationPort.existsRelationWithUserAndIdRestaurant(idRestaurant)).thenReturn(true);

        NoDataFoundException exception = assertThrows(NoDataFoundException.class, () -> {
            orderUseCase.findAllPagedOrdersByIdStatus(idRestaurant, status, page, sizePage, token);
        });

        assertEquals("No orders found", exception.getMessage());
        verify(tokenValidationPort, times(1)).verifyRoleInToken(token, "ROLE_EMPLOYEE");
        verify(userValidationComunicationPort, times(1)).existsRelationWithUserAndIdRestaurant(idRestaurant);
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
        when(userValidationComunicationPort.existsRelationWithUserAndIdRestaurant(idRestaurant)).thenReturn(true);


        List<OrderWithDetailDto> allPagedOrdersByIdStatus = orderUseCase.findAllPagedOrdersByIdStatus(idRestaurant, status, page, sizePage, token);


        verify(tokenValidationPort, times(1)).verifyRoleInToken(token, "ROLE_EMPLOYEE");
        verify(userValidationComunicationPort, times(1)).existsRelationWithUserAndIdRestaurant(idRestaurant);
        verify(orderPersistencePort, times(1)).getOrdersByRestaurantAndStatus(page, sizePage, idRestaurant, status);
    }

    @Test
    @DisplayName("Should assign the order to an employee when the order is not taken or activated")
    void assignOrder_successful() {
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
        when(userValidationComunicationPort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);
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
        when(userValidationComunicationPort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(false);

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
        when(userValidationComunicationPort.existsRelationWithUserAndIdRestaurant(order.getRestaurant().getId())).thenReturn(true);

        assertThrows(UserHasNoPermissionException.class, () -> orderUseCase.assignOrder(idOrder, token));
    }

    @Test
    void findById_whenNullReturned() {
        Long idOrder = 1L;
        when(orderPersistencePort.findById(idOrder)).thenReturn(null);
        assertThrows(NoDataFoundException.class, () -> orderUseCase.findById(idOrder));
    }

}