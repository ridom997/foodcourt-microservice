package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePerformanceResponseDto {
    @Schema(example = "11")
    private Long idEmployee;
    @Schema(example = "680")
    private Integer averageTimeInSeconds;
    @Schema(example = "4")
    private Long numberOfOrders;
}
