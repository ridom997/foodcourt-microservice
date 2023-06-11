package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderAndStatusMessagingResponseDto {
    private OrderResponseDto order;
    @Schema(example = "false")
    private Boolean errorSendingSms;
}
