package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndRestaurantOwnerIdDto;

public interface IDishServicePort {
    void saveDish(DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto);
}
