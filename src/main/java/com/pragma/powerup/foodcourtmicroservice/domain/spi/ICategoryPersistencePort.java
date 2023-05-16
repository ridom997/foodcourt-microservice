package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;

public interface ICategoryPersistencePort {
    Category findById(Long id);
}
