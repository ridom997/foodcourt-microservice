package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.request;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewOrderRequestDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.NewOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {

    NewOrderDto toDomainObject(NewOrderRequestDto newOrderRequestDto);
}
