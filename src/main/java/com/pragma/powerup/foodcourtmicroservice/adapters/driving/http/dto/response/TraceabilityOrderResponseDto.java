package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraceabilityOrderResponseDto {
    @Schema(example = "PENDING")
    private String previousStatus;
    @Schema(example = "IN PROGRESS")
    private String newStatus;
    @Schema(example = "20")
    private Long idEmployee;
    private LocalDateTime date;
}
