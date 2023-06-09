package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndAmountDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.OrderDish;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderDishPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDishUseCaseTest {

    @Mock
    private IOrderDishPersistencePort orderDishPersistencePort;

    @InjectMocks
    private OrderDishUseCase orderDishUseCase;

    private Order order;
    private List<DishAndAmountDto> dishAndAmountDtoList = new ArrayList<>();
    @BeforeEach
    void setUp() {
        order = new Order(1L, LocalDateTime.now(), 1, 1L, new Restaurant(1L));
        dishAndAmountDtoList.add(new DishAndAmountDto(new Dish(),1));
        dishAndAmountDtoList.add(new DishAndAmountDto(new Dish(),2));
    }

    @Test
    void saveListOrderDish_success() {
        List<OrderDish> orderDishListExpected = new ArrayList<>();
        when(orderDishPersistencePort.saveAll(anyList())).thenReturn(orderDishListExpected);

        List<OrderDish> result = orderDishUseCase.saveListOrderDish(order, dishAndAmountDtoList);

        verify(orderDishPersistencePort,times(1)).saveAll(anyList());
        assertEquals(orderDishListExpected,result);
    }

    @Test
    void saveListOrderDish_dishListIsEmpty() {
        List<DishAndAmountDto> dishListWithAmount = new ArrayList<>();

        List<OrderDish> result = orderDishUseCase.saveListOrderDish(order, dishListWithAmount);

        assertTrue(result.isEmpty());
    }


    @Test
    void getAllOrderDishByOrder_whenOrderHasNoDishes() {
        when(orderDishPersistencePort.getAllByOrder(order)).thenReturn(new ArrayList<>());

        assertThrows(NoDataFoundException.class, () -> {
            orderDishUseCase.getAllOrderDishByOrder(order);
        });

        verify(orderDishPersistencePort, times(1)).getAllByOrder(order);
    }

    @Test
    void getAllOrderDishByOrder_succesfully() {
        List<OrderDish> orderDishList = new ArrayList<>();
        orderDishList.add(new OrderDish(order, new Dish(1L, "Pizza"), 2));
        orderDishList.add(new OrderDish(order, new Dish(2L, "Burger"), 1));

        when(orderDishPersistencePort.getAllByOrder(order)).thenReturn(orderDishList);

        List<OrderDish> result = orderDishUseCase.getAllOrderDishByOrder(order);

        assertEquals(orderDishList, result);
        verify(orderDishPersistencePort, times(1)).getAllByOrder(order);
    }

}