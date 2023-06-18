package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper.response;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.EmployeePerformanceDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmployeePerformanceMapper {
    EmployeePerformanceResponseDto toEmployeePerformanceResponseDto(EmployeePerformanceDto employeePerformanceDto);
}
