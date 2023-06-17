package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDurationInfoResponseDto {
    @Schema(example = "1")
    private Long idOrder;
    private LocalDateTime creationTime;
    private LocalDateTime endTime;
    @Schema(example = "412")
    private Long durationInSeconds;
    @Schema(example = "DELIVERED")
    private String finalStatus;
}
