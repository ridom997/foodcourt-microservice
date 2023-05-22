package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.EditDishRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewDishInfoRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.request.IDishRequestMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response.IDishResponseMapper;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndRestaurantOwnerIdDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.EditDishInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DishHandlerImpl implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void saveDish(NewDishInfoRequestDto dishInfoRequestDto) {
        Dish dish = dishRequestMapper.mapToDish(dishInfoRequestDto);
        dishServicePort.saveDish(new DishAndRestaurantOwnerIdDto(dish,dishInfoRequestDto.getIdOwnerRestaurant()));
    }
    @Override
    public DishResponseDto editDish(Long idDish, EditDishRequestDto editDishRequestDto) {
        EditDishInfoDto editDishInfoDto = new EditDishInfoDto(editDishRequestDto.getIdOwnerRestaurant(),
                idDish,editDishRequestDto.getPrice(),editDishRequestDto.getDescription());
        return dishResponseMapper.toDishResponseDto(dishServicePort.editDish(editDishInfoDto));
    }
}
