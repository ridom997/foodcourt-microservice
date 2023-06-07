package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client.UserFeignClient;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.dto.request.UserAndRoleRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.FailConnectionToExternalMicroserviceException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.NoRestaurantAssociatedWithUserException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.UserNotFoundFeignException;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserValidationComunicationPort;
import feign.FeignException;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@AllArgsConstructor
@Component
public class UserValidationFeignAdapter implements IUserValidationComunicationPort {

    private final UserFeignClient userFeignClient;

    @Override
    public Boolean userHasRole(Long idUser, Long idRole) {
        try {
            Map<String, Boolean> response = userFeignClient.userHasRole(new UserAndRoleRequestDto(idUser, idRole));
            return response.get("result");
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundFeignException(e);
        } catch (RetryableException e){
            throw new FailConnectionToExternalMicroserviceException();
        }
    }

    @Override
    public Boolean existsRelationWithUserAndIdRestaurant(Long idRestaurant) {
        try {
            Map<String, Boolean> response = userFeignClient.existsRelationWithUserAndIdRestaurant(idRestaurant);
            return response.get("isARestaurantEmployee");
        } catch (FeignException.NotFound e) {
            throw new NoRestaurantAssociatedWithUserException();
        } catch (RetryableException e){
            throw new FailConnectionToExternalMicroserviceException();
        }
    }
}
