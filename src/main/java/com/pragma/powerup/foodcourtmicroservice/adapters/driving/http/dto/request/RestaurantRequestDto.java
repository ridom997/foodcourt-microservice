package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request;

import com.pragma.powerup.foodcourtmicroservice.configuration.Constants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantRequestDto {
    @NotBlank
    @Size(max = 100, min = 1)
    @Pattern(regexp = Constants.ALPHANUMERIC_BUT_NOT_ONLY_NUMBERS_REGEX, message = "Name is not valid, it must have at least one non-numeric character")
    private String name;

    @NotBlank
    @Size(max = 100, min = 1)
    private String address;

    @NotBlank
    @Size(max = 13, min = 1)
    @Pattern(regexp = Constants.PHONE_REGEX, message = "Phone is in bad format")
    private String phone;

    @NotBlank
    @Size(max = 100, min = 1)
    private String urlLogo;

    @NotBlank
    @Size(max = 100, min = 1)
    @Pattern(regexp = Constants.ONLY_NUMBERS_REGEX, message = "Nit must have only numbers")
    private String nit;

    @NotNull
    private Long idOwner;
}
