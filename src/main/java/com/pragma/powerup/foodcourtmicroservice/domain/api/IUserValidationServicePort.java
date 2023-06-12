package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderActorsDto;

public interface IUserValidationServicePort {
    OrderActorsDto findClientAndEmployeeInfo(Long idClient, Long idEmployee);
    Boolean existsRelationWithUserAndIdRestaurant(Long idRestaurant);
}
