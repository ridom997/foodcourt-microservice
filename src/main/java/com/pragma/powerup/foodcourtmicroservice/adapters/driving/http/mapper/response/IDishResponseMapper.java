package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.CategoryResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {

    @Named("mapToCategoryResponseDto")
    default CategoryResponseDto mapToCategoryResponseDto(Category category){
        return new CategoryResponseDto(category.getId(), category.getName());
    }

    @Mapping(source = "dish.restaurant.id", target = "idRestaurant")
    @Mapping(source = "dish.category", target = "category", qualifiedByName = "mapToCategoryResponseDto")
    DishResponseDto toDishResponseDto(Dish dish);
}
