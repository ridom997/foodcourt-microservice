package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IdDishAndAmountRequestDto {
    @NotNull
    @Schema(example = "100")
    private Long idDish;

    @NotNull
    @Schema(example = "2")
    private Integer amount;
}
