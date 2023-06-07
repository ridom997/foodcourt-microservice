package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishResponseDto {
    @Schema(example = "1")
    private Long idDish;
    @Schema(example = "Bandeja paisa")
    private String nameDish;
    @Schema(example = "3")
    private Integer amount;
}
