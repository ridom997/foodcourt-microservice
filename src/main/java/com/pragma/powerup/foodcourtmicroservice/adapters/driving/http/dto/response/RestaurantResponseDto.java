package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponseDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Pragma corporation food")
    private String name;
    @Schema(example = "www.pragmafood.com")
    private String urlLogo;
}
