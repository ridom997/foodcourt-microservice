package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class OrderResponseDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "105")
    private Long idClient;
    @Schema(example = "2023-06-05")
    private LocalDateTime date;
    @Schema(example = "PENDING")
    private String status;
    @Schema(example = "1")
    private Long idChef;
    @Schema(example = "100")
    private Long idRestaurant;
    @Schema(example = "Food Pragma")
    private String nameRestaurant;

    @JsonInclude(JsonInclude.Include.NON_NULL) //do not show this variable in the response if it's null.
    private String deliveryPin;
}
