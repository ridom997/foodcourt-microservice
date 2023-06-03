package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.EditDishRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewDishInfoRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;

import java.util.List;

public interface IDishHandler {
    DishResponseDto saveDish(NewDishInfoRequestDto dishInfoRequestDto);
    DishResponseDto editDish(Long idDish, EditDishRequestDto editDishRequestDto);
    DishResponseDto changeStatusDish(Long idDish, Boolean status);

    List<DishResponseDto> getPagedDishesByRestaurantAndOptionalCategory(Integer page, Integer sizePage, Long idRestaurant, Long idCategory);
}
