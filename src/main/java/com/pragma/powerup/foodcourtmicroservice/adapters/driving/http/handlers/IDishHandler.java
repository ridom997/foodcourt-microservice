package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.DishInfoRequestDto;

public interface IDishHandler {
    void saveDish(DishInfoRequestDto dishInfoRequestDto);
}
