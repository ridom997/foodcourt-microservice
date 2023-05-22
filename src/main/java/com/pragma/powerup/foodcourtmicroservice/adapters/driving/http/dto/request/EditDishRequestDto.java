package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditDishRequestDto {
    @NotNull
    @Schema(example = "2")
    private Long idOwnerRestaurant;

    @NotNull
    @Min(value = 1, message = "Price must be grater than 0")
    @Schema(example = "20000")
    private Integer price;

    @NotBlank
    private String description;
}
