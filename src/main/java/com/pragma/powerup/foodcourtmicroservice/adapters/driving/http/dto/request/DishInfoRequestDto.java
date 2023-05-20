package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishInfoRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String urlImage;

    @NotNull
    @Min(value = 1, message = "Price must be grater than 0")
    private Integer price;

    @NotNull
    private Long idOwnerRestaurant;

    @NotNull
    private Long idCategory;

    @NotNull
    private Long idRestaurant;
}
