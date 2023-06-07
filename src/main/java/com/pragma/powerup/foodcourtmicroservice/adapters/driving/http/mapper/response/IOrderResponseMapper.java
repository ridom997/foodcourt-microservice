package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderDishResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderWithDetailResponseDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderWithDetailDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.OrderDish;
import com.pragma.powerup.foodcourtmicroservice.domain.utils.OrderUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {

    @Named("mapStatusIntToString")
    default String mapStatusIntToString(Integer integerStatus){
        return OrderUtils.mapOrderStatusToString(integerStatus);
    }

    @Mapping(source = "order.restaurant.id", target = "idRestaurant")
    @Mapping(source = "order.restaurant.name", target = "nameRestaurant")
    @Mapping(source = "order.status", target = "status", qualifiedByName = "mapStatusIntToString")
    OrderResponseDto toOrderResponseDto(Order order);


    @Named("mapOrderDishToResponseDto")
    default OrderDishResponseDto toOrderDishResponseDto(OrderDish orderDish){
        return new OrderDishResponseDto(orderDish.getDish().getId(), orderDish.getDish().getName(), orderDish.getAmount());
    }

    @Named("mapListToOrderDishResponseDto")
    default List<OrderDishResponseDto> toOrderDishResponseDto(List<OrderDish> detail){
        return detail.stream()
                .map(orderDish -> new OrderDishResponseDto(orderDish.getDish().getId(), orderDish.getDish().getName(),orderDish.getAmount()))
                .toList();
    }

    @Mapping(source = "orderWithDetailDto.detail", target = "detail", qualifiedByName = "mapListToOrderDishResponseDto")
    OrderWithDetailResponseDto toOrderWithDetailResponseDto(OrderWithDetailDto orderWithDetailDto);
}
