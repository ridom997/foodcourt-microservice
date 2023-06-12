package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderActorsDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.UserBasicInfoDto;

public interface IUserValidationServicePort {
    OrderActorsDto findClientAndEmployeeInfo(Long idClient, Long idEmployee);

    UserBasicInfoDto findClientInfo(Long idClient);
    Boolean existsRelationWithUserAndIdRestaurant(Long idRestaurant);
}
