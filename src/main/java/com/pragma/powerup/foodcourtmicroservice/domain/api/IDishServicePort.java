package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndRestaurantOwnerIdDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.EditDishInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;

public interface IDishServicePort {
    void saveDish(DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto, String token);
    Dish editDish(EditDishInfoDto editDishInfoDto, String token);
    Dish findById(Long id);
}
