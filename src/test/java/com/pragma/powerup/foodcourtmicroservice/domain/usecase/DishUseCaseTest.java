package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.ICategoryServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndRestaurantOwnerIdDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IDishPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistancePort;

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private ICategoryServicePort categoryServicePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Category category;
    private Restaurant restaurant;
    private DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto;
    @BeforeEach
    void setUp() {
        dishUseCase = new DishUseCase(dishPersistancePort, categoryServicePort, restaurantServicePort);
        category = new Category(1L,"name","description");
        restaurant = new Restaurant();

    }

    @Test
    void testSaveDish_validatingCategory() {
        final Dish dish = new Dish(1L,"name",category,"description",12,restaurant,"urlImage");
        final DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto = new DishAndRestaurantOwnerIdDto(dish,1L);
        when(restaurantServicePort.findById(restaurant.getId())).thenReturn(restaurant);
        when(restaurantServicePort.isTheRestaurantOwner(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(),restaurant)).thenReturn(true);
        when(categoryServicePort.findById(category.getId())).thenReturn(category);

        dishUseCase.saveDish(dishAndRestaurantOwnerIdDto);

        verify(restaurantServicePort).isTheRestaurantOwner(1L,restaurant);
        verify(restaurantServicePort).findById(restaurant.getId());
        verify(categoryServicePort).findById(category.getId());
        verify(dishPersistancePort).saveDish(dish);
    }
}