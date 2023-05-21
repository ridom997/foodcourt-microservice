package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;

public interface IDishPersistencePort {
    void saveDish(Dish dish);
    Dish findById(Long id);
}
