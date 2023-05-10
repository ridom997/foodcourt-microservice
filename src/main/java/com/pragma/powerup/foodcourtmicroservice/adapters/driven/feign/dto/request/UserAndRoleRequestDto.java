package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAndRoleRequestDto {
    private Long idUser;
    private Long idRole;
}