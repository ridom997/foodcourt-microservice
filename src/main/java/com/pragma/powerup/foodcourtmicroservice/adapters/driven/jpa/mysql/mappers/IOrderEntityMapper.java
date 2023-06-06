package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

    @Named("mapToRestaurantEntity")
    default RestaurantEntity mapRestaurantToRestaurantEntity(Restaurant restaurant){
        return new RestaurantEntity(restaurant.getId());
    }

    @Named("mapToRestaurant")
    default Restaurant mapRestaurantEntityToRestaurant(RestaurantEntity restaurantEntity){
        return new Restaurant(restaurantEntity.getId(),restaurantEntity.getName());
    }

    @Mapping(source = "order.restaurant", target = "idRestaurant", qualifiedByName = "mapToRestaurantEntity")
    OrderEntity mapToEntity(Order order);

    @Mapping(source = "orderEntity.idRestaurant", target = "restaurant", qualifiedByName = "mapToRestaurant")
    Order mapToOrder(OrderEntity orderEntity);
}
