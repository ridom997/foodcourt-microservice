package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {

    @Mapping(source = "dish.category.id", target = "idCategory")
    @Mapping(source = "dish.restaurant.id", target = "idRestaurant")
    DishResponseDto toDishResponseDto(Dish dish);
}
