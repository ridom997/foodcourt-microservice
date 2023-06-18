package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.*;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderAndStatusMessagingDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderWithDetailDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.HistoryOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.TraceabilityOrderDto;
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

    OrderAndStatusMessagingResponseDto toOrderAndStatusMessagingResponseDto(OrderAndStatusMessagingDto orderAndStatusMessagingDto);

    @Mapping(source = "traceabilityOrderDto.previousStatus", target = "previousStatus", qualifiedByName = "mapStatusIntToString")
    @Mapping(source = "traceabilityOrderDto.newStatus", target = "newStatus", qualifiedByName = "mapStatusIntToString")
    TraceabilityOrderResponseDto mapToTraceabilityOrderResponseDto(TraceabilityOrderDto traceabilityOrderDto);


    @Mapping(source = "historyOrderDto.actualStatus", target = "actualStatus", qualifiedByName = "mapStatusIntToString")
    HistoryOrderResponseDto mapToHistoryOrderResponseDto(HistoryOrderDto historyOrderDto);

    @Mapping(source = "orderDurationInfoDto.finalStatus", target = "finalStatus", qualifiedByName = "mapStatusIntToString")
    OrderDurationInfoResponseDto toOrderDurationInfoResponseDto(OrderDurationInfoDto orderDurationInfoDto);
}
