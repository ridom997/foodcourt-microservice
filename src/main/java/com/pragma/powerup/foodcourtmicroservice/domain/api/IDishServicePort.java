package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndRestaurantOwnerIdDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.EditDishInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;

import java.util.List;
import java.util.Optional;

public interface IDishServicePort {
    Dish saveDish(DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto, String token);
    Dish editDish(EditDishInfoDto editDishInfoDto, String token);
    Dish findById(Long id);

    Dish changeStatusDish(Long idDish, Boolean status, String token);

    List<Dish> getPagedDishesByRestaurantAndOptionalCategory(Long idRestaurant, Integer page, Integer sizePage, Optional<Long> idCategory, String token);
}
