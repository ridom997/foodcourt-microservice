package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.request;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewDishInfoRequestDto;
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
public interface IDishRequestMapper {

    @Named("mapToCategory")
    default Category mapToCategory(Long idCategory){
        return new Category(idCategory);
    }

    @Named("mapToRestaurant")
    default Restaurant mapToRestaurant(Long idRestaurant){
        return new Restaurant(idRestaurant);
    }

    @Mapping(source = "dishInfoRequestDto.idCategory", target = "category", qualifiedByName = "mapToCategory")
    @Mapping(source = "dishInfoRequestDto.idRestaurant", target = "restaurant", qualifiedByName = "mapToRestaurant")
    Dish mapToDish(NewDishInfoRequestDto dishInfoRequestDto);

}
