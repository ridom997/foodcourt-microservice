package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.ICategoryServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
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


public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final ICategoryServicePort categoryServicePort;
    private final IRestaurantServicePort restaurantServicePort;

    private final ITokenValidationPort tokenValidationPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, ICategoryServicePort categoryServicePort, IRestaurantServicePort restaurantServicePort, ITokenValidationPort tokenValidationPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.categoryServicePort = categoryServicePort;
        this.restaurantServicePort = restaurantServicePort;
        this.tokenValidationPort = tokenValidationPort;
    }

    @Override
    public void saveDish(DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto, String token) {
        Boolean userIsInToken = tokenValidationPort.userIsInToken(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(), token);
        if (userIsInToken.equals(false))
            throw new UserHasNoPermissionException();
        Restaurant restaurant = restaurantServicePort.findById(dishAndRestaurantOwnerIdDto.getDish().getRestaurant().getId());
        Category category = categoryServicePort.findById(dishAndRestaurantOwnerIdDto.getDish().getCategory().getId());
        if(Boolean.FALSE.equals(restaurantServicePort.isTheRestaurantOwner(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(), restaurant))){
            throw new UserHasNoPermissionException();
        }
        dishAndRestaurantOwnerIdDto.getDish().setCategory(category);
        dishAndRestaurantOwnerIdDto.getDish().setRestaurant(restaurant);
        dishPersistencePort.saveDish(dishAndRestaurantOwnerIdDto.getDish());
    }

    @Override
    public Dish editDish(EditDishInfoDto editDishInfoDto, String token) {
        Boolean userIsInToken = tokenValidationPort.userIsInToken(editDishInfoDto.getIdOwnerRestaurant(), token);
        if (userIsInToken.equals(false))
            throw new UserHasNoPermissionException();
        Dish dish = findById(editDishInfoDto.getIdDish());
        if(Boolean.FALSE.equals(restaurantServicePort.isTheRestaurantOwner(editDishInfoDto.getIdOwnerRestaurant(), dish.getRestaurant()))){
            throw new UserHasNoPermissionException();
        }
        dish.setPrice(editDishInfoDto.getPrice());
        dish.setDescription(editDishInfoDto.getDescription());
        dishPersistencePort.saveDish(dish);
        return dish;
    }

    @Override
    public Dish findById(Long id) {
        Dish dish = dishPersistencePort.findById(id);
        if (dish == null)
            throw new NoDishFoundException();
        return dish;
    }


}
