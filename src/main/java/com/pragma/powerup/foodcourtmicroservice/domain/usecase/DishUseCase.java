package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.ICategoryServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndRestaurantOwnerIdDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoCategoryFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoRestaurantFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.UserHasNoPermissionException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IDishPersistencePort;


public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final ICategoryServicePort categoryServicePort;
    private final IRestaurantServicePort restaurantServicePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, ICategoryServicePort categoryServicePort, IRestaurantServicePort restaurantServicePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.categoryServicePort = categoryServicePort;
        this.restaurantServicePort = restaurantServicePort;
    }

    @Override
    public void saveDish(DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto) {
        Restaurant restaurant = restaurantServicePort.findById(dishAndRestaurantOwnerIdDto.getDish().getRestaurant().getId());
        if(restaurant == null){
            throw new NoRestaurantFoundException();
        }
        Category category = categoryServicePort.findById(dishAndRestaurantOwnerIdDto.getDish().getCategory().getId());
        if(category == null){
            throw new NoCategoryFoundException();
        }
        if(Boolean.FALSE.equals(restaurantServicePort.isTheRestaurantOwner(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(), restaurant))){
            throw new UserHasNoPermissionException();
        }
        dishAndRestaurantOwnerIdDto.getDish().setCategory(category);
        dishAndRestaurantOwnerIdDto.getDish().setRestaurant(restaurant);
        dishPersistencePort.saveDish(dishAndRestaurantOwnerIdDto.getDish());
    }



}
