package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.EmployeePerformanceDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoRestaurantFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantOrderCommonUseCaseTest {

    @Mock
    private IRestaurantPersistencePort mockRestaurantPersistencePort;
    @Mock
    private IOrderPersistencePort mockOrderPersistencePort;

    private RestaurantOrderCommonUseCase restaurantOrderCommonUseCaseUnderTest;

    @BeforeEach
    void setUp() {
        restaurantOrderCommonUseCaseUnderTest = new RestaurantOrderCommonUseCase(mockRestaurantPersistencePort,
                mockOrderPersistencePort);
    }

    @Test
    void testFindRestaurantById_successfully() {
        final Restaurant restaurant = new Restaurant(0L, "name", "address", 0L, "phone", "urlLogo", "nit");
        when(mockRestaurantPersistencePort.findById(0L)).thenReturn(restaurant);

        // Run the test
        final Restaurant result = restaurantOrderCommonUseCaseUnderTest.findRestaurantById(0L);

        assertEquals(restaurant,result);
    }

    @Test
    void testFindRestaurantById_restaurantDoesNotExist() {
        // Setup
        when(mockRestaurantPersistencePort.findById(0L)).thenReturn(null);

        // Run the test
        assertThrows(NoRestaurantFoundException.class, () -> restaurantOrderCommonUseCaseUnderTest.findRestaurantById(0L));
    }

    @Test
    void testGetDurationOfFinalizedOrdersByRestaurant_successfully() {
        // Setup
        final Restaurant restaurant = new Restaurant(0L, "name", "address", 0L, "phone", "urlLogo", "nit");

        final List<OrderDurationInfoDto> orderDurationInfoDtos = List.of(
                new OrderDurationInfoDto(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 30), 0));
        when(mockOrderPersistencePort.findAllPagedCompletedOrdersByIdRestaurant(0L, 0, 0))
                .thenReturn(orderDurationInfoDtos);

        // Run the test
        final List<OrderDurationInfoDto> result = restaurantOrderCommonUseCaseUnderTest.getDurationOfFinalizedOrdersByRestaurant(
                restaurant, 0, 0);

        assertEquals(30,result.get(0).getDurationInSeconds());
        verify(mockOrderPersistencePort,times(1)).findAllPagedCompletedOrdersByIdRestaurant(0L, 0, 0);
    }

    @Test
    void testGetDurationOfFinalizedOrdersByRestaurant_IOrderPersistencePortReturnsNoItems() {
        // Setup
        final Restaurant restaurant = new Restaurant(0L, "name", "address", 0L, "phone", "urlLogo", "nit");
        when(mockOrderPersistencePort.findAllPagedCompletedOrdersByIdRestaurant(0L, 0, 0))
                .thenReturn(Collections.emptyList());

        assertThrows(NoDataFoundException.class, () -> restaurantOrderCommonUseCaseUnderTest.getDurationOfFinalizedOrdersByRestaurant(restaurant, 0,
                0));
    }

    @Test
    void testGetRankingOfEmployeesByRestaurant_successfully() {
        // Setup
        Long idRestaurant = 3L;
        final List<EmployeePerformanceDto> orderDurationInfoDtos = List.of(new EmployeePerformanceDto(), new EmployeePerformanceDto());
        when(mockOrderPersistencePort.getRankingOfEmployeesByRestaurant(idRestaurant, 0, 0))
                .thenReturn(orderDurationInfoDtos);

        // Run the test
        List<EmployeePerformanceDto> result = restaurantOrderCommonUseCaseUnderTest.getRankingOfEmployeesByRestaurant(
                idRestaurant, 0, 0);

        assertEquals(2,result.size());
        verify(mockOrderPersistencePort,times(1)).getRankingOfEmployeesByRestaurant(idRestaurant, 0, 0);
    }

    @Test
    void testGetRankingOfEmployeesByRestaurant_IOrderPersistencePortReturnsNoItems() {
        // Setup
        Long idRestaurant = 3L;
        when(mockOrderPersistencePort.getRankingOfEmployeesByRestaurant(idRestaurant, 0, 0))
                .thenReturn(Collections.emptyList());

        assertThrows(NoDataFoundException.class, () -> restaurantOrderCommonUseCaseUnderTest.getRankingOfEmployeesByRestaurant(idRestaurant, 0,
                0));
    }
}
