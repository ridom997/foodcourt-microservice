package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishResponseDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Plato ejecutivo 1")
    private String name;
    private CategoryResponseDto category;
    @Schema(example = "Frijoles, arroz y carne molida")
    private String description;
    @Schema(example = "13000")
    private Integer price;
    @Schema(example = "1")
    private Long idRestaurant;
    @Schema(example = "https://cdn0.recetasgratis.net/es/posts/1/4/0/arroz_con_carne_molida_74041_600.webp")
    private String urlImage;
    @Schema(example = "true")
    private Boolean active;
}
