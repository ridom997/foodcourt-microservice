package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.EditDishRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewDishInfoRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;

public interface IDishHandler {
    void saveDish(NewDishInfoRequestDto dishInfoRequestDto);
    DishResponseDto editDish(Long idDish, EditDishRequestDto editDishRequestDto);
}
