package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishEntityMapper {

    @Named("mapToCategoryEntity")
    default CategoryEntity mapToCategoryEntity(Category category){
        return new CategoryEntity(category.getId());
    }

    @Named("mapToRestaurantEntity")
    default RestaurantEntity mapToRestaurantEntity(Restaurant restaurant){
        return new RestaurantEntity(restaurant.getId());
    }

    @Mapping(source = "dish.category", target = "idCategory", qualifiedByName = "mapToCategoryEntity")
    @Mapping(source = "dish.restaurant", target = "idRestaurant", qualifiedByName = "mapToRestaurantEntity")
    DishEntity toEntity(Dish dish);
    Dish toDish(DishEntity dishEntity);
}
