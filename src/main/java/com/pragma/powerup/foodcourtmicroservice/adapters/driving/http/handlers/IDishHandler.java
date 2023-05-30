package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.EditDishRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewDishInfoRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;

public interface IDishHandler {
    DishResponseDto saveDish(NewDishInfoRequestDto dishInfoRequestDto);
    DishResponseDto editDish(Long idDish, EditDishRequestDto editDishRequestDto);
    DishResponseDto changeStatusDish(Long idDish, Boolean status);
}
