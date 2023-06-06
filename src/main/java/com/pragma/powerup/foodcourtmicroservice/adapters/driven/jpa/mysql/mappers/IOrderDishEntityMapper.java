package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderDishEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderDishEntityMapper {

    @Named("mapToOrderEntity")
    default OrderEntity mapToOrderEntity(Order order){
        return new OrderEntity(order.getId());
    }

    @Named("mapToDishEntity")
    default DishEntity mapToDishEntity(Dish dish){
        return new DishEntity(dish.getId());
    }

    @Mapping(source = "orderDish.order.id", target = "id.idOrder")
    @Mapping(source = "orderDish.dish.id", target = "id.idDish")
    @Mapping(source = "orderDish.order", target = "idOrder", qualifiedByName = "mapToOrderEntity")
    @Mapping(source = "orderDish.dish", target = "idDish", qualifiedByName = "mapToDishEntity")
    OrderDishEntity mapToEntity(OrderDish orderDish);


    OrderDish toOrderDish(OrderDishEntity orderDishEntity);
}
