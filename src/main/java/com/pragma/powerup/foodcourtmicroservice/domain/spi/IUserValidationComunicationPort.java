package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.UserBasicInfoDto;

import java.util.List;

public interface IUserValidationComunicationPort {
    Boolean userHasRole(Long idUser, Long idRole);
    Boolean existsRelationWithUserAndIdRestaurant(Long idRestaurant);

    List<UserBasicInfoDto> getBasicInfoOfUsers(List<Long> userIdList);
}
