package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.utils.OrderUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {

    @Named("mapStatusIntToString")
    default String mapStatusIntToString(Integer integerStatus){
        return OrderUtils.mapOrderStatusToString(integerStatus);
    }

    @Mapping(source = "order.restaurant.id", target = "idRestaurant")
    @Mapping(source = "order.status", target = "status", qualifiedByName = "mapStatusIntToString")
    OrderResponseDto toOrderResponseDto(Order order);
}
