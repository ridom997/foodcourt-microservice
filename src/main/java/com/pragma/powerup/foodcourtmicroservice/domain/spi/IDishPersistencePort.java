package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;

import java.util.List;

public interface IDishPersistencePort {
    Dish saveDish(Dish dish);
    Dish findById(Long id);
    List<Dish> getDishesByRestaurantAndCategory(Integer page, Integer sizePage, Long idRestaurant, Long idCategory);
}
