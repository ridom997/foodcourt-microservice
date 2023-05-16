package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;

public interface ICategoryServicePort {
    Category findById(Long id);
}
