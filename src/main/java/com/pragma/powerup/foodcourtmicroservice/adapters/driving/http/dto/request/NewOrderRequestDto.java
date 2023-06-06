package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewOrderRequestDto {
    @NotNull
    @Schema(example = "100")
    private Long idRestaurant;

    @NotEmpty
    private List<@Valid IdDishAndAmountRequestDto> dishes;
}
