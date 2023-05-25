package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.ICategoryServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndRestaurantOwnerIdDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.EditDishInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDishFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.UserHasNoPermissionException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IDishPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistancePort;

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private ICategoryServicePort categoryServicePort;

    @Mock
    private ITokenValidationPort tokenValidationPort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Category category;
    private Restaurant restaurant;

    private Dish idealDish;

    @BeforeEach
    void setUp() {
        dishUseCase = new DishUseCase(dishPersistancePort, categoryServicePort, restaurantServicePort, tokenValidationPort);
        category = new Category(1L,"name","description");
        restaurant = new Restaurant();
        idealDish = new Dish();
        idealDish.setId(1L);
        idealDish.setName("name");
        idealDish.setCategory(category);
        idealDish.setDescription("description");
        idealDish.setPrice(12);
        idealDish.setRestaurant(restaurant);
        idealDish.setUrlImage("url");
    }

    @Test
    void saveDishTest_success() {
        final DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto = new DishAndRestaurantOwnerIdDto(idealDish,1L);
        when(restaurantServicePort.findById(restaurant.getId())).thenReturn(restaurant);
        when(restaurantServicePort.isTheRestaurantOwner(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(),restaurant)).thenReturn(true);
        when(categoryServicePort.findById(category.getId())).thenReturn(category);
        when(tokenValidationPort.userIsInToken(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(),"token")).thenReturn(true);

        dishUseCase.saveDish(dishAndRestaurantOwnerIdDto,"token");

        verify(restaurantServicePort).isTheRestaurantOwner(1L,restaurant);
        verify(restaurantServicePort).findById(restaurant.getId());
        verify(categoryServicePort).findById(category.getId());
        verify(dishPersistancePort).saveDish(idealDish);
    }

    @Test
    void saveDishTest_userHasNoPermissionException() {
        final DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto = new DishAndRestaurantOwnerIdDto(idealDish,1L);
        when(restaurantServicePort.findById(restaurant.getId())).thenReturn(restaurant);
        when(restaurantServicePort.isTheRestaurantOwner(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(),restaurant)).thenReturn(false);
        when(categoryServicePort.findById(category.getId())).thenReturn(category);
        when(tokenValidationPort.userIsInToken(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(),"token")).thenReturn(true);

        assertThrows(UserHasNoPermissionException.class, () -> dishUseCase.saveDish(dishAndRestaurantOwnerIdDto,"token"));

        verify(restaurantServicePort).isTheRestaurantOwner(1L,restaurant);
        verify(restaurantServicePort).findById(restaurant.getId());
        verify(categoryServicePort).findById(category.getId());
    }

    @Test
    void saveDishTest_userHasNoPermissionBecauseUserIsNotInTokenException() {
        final DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto = new DishAndRestaurantOwnerIdDto(idealDish,1L);
        when(tokenValidationPort.userIsInToken(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(),"token")).thenReturn(false);

        assertThrows(UserHasNoPermissionException.class, () -> dishUseCase.saveDish(dishAndRestaurantOwnerIdDto,"token"));

        verify(dishPersistancePort, times(0)).saveDish(idealDish);
    }

    @Test
    void editDishTest_success() {
        Long idDish = 1L;
        final EditDishInfoDto editDishInfoDto = new EditDishInfoDto(1L,idDish,3,"description edited");
        when(dishPersistancePort.findById(idDish)).thenReturn(idealDish);
        when(restaurantServicePort.isTheRestaurantOwner(editDishInfoDto.getIdOwnerRestaurant(),idealDish.getRestaurant())).thenReturn(true);
        when(tokenValidationPort.userIsInToken(editDishInfoDto.getIdOwnerRestaurant(),"token")).thenReturn(true);

        Dish resultDish = dishUseCase.editDish(editDishInfoDto,"token");

        assertEquals("description edited",resultDish.getDescription());
        assertEquals(3,resultDish.getPrice());
        verify(restaurantServicePort).isTheRestaurantOwner(1L,idealDish.getRestaurant());
        verify(dishPersistancePort).findById(idDish);
        verify(dishPersistancePort).saveDish(idealDish);
    }

    @Test
    void editDishTest_userHasNoPermissionException() {
        Long idDish = 1L;
        final EditDishInfoDto editDishInfoDto = new EditDishInfoDto(1L,idDish,3,"description edited");
        when(dishPersistancePort.findById(idDish)).thenReturn(idealDish);
        when(restaurantServicePort.isTheRestaurantOwner(editDishInfoDto.getIdOwnerRestaurant(),idealDish.getRestaurant())).thenReturn(false);
        when(tokenValidationPort.userIsInToken(editDishInfoDto.getIdOwnerRestaurant(),"token")).thenReturn(true);

        assertThrows(UserHasNoPermissionException.class,() -> dishUseCase.editDish(editDishInfoDto,"token"));

        verify(restaurantServicePort).isTheRestaurantOwner(1L,idealDish.getRestaurant());
        verify(dishPersistancePort).findById(idDish);
        verify(dishPersistancePort, times(0)).saveDish(idealDish);
    }

    @Test
    void editDishTest_userHasNoPermissionBecauseIsNotInTokenException() {
        Long idDish = 1L;
        final EditDishInfoDto editDishInfoDto = new EditDishInfoDto(1L,idDish,3,"description edited");
        when(tokenValidationPort.userIsInToken(editDishInfoDto.getIdOwnerRestaurant(),"token")).thenReturn(false);

        assertThrows(UserHasNoPermissionException.class,() -> dishUseCase.editDish(editDishInfoDto,"token"));

        verify(dishPersistancePort, times(0)).saveDish(idealDish);
    }

    @Test
    void findByIdTest_success() {
        Long idDish = 1L;
        when(dishPersistancePort.findById(idDish)).thenReturn(idealDish);

        Dish result = dishUseCase.findById(idDish);

        verify(dishPersistancePort).findById(idDish);
        assertEquals(idealDish,result);
    }

    @Test
    void findByIdTest_noDishFoundException() {
        Long idDish = 1L;
        when(dishPersistancePort.findById(idDish)).thenReturn(null);

        assertThrows(NoDishFoundException.class, () -> dishUseCase.findById(idDish));

        verify(dishPersistancePort).findById(idDish);
    }

}