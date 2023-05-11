package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.configuration.Constants;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.*;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistancePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserValidationComunicationPort;

public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistancePort restaurantPersistancePort;
    private final IUserValidationComunicationPort userValidationComunicationPort;

    public RestaurantUseCase(IRestaurantPersistancePort restaurantPersistancePort, IUserValidationComunicationPort userValidationComunicationPort) {
        this.restaurantPersistancePort = restaurantPersistancePort;
        this.userValidationComunicationPort = userValidationComunicationPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        try{
            if (!restaurant.getName().matches(Constants.ALPHANUMERIC_BUT_NOT_ONLY_NUMBERS_REGEX))
                throw new NotValidNameRestaurantException();
            if (!restaurant.getPhone().matches(Constants.PHONE_REGEX))
                throw new InvalidPhoneException();
            if (!restaurant.getNit().matches(Constants.ONLY_NUMBERS_REGEX))
                throw new NotOnlyNumbersException();
            Boolean userHasRole = userValidationComunicationPort.userHasRole(restaurant.getIdOwner(), Constants.OWNER_ROLE_ID);
            if(userHasRole.equals(false))
                throw new UserHasNoPermissionException();
            restaurantPersistancePort.saveRestaurant(restaurant);
        } catch (NullPointerException e){
            throw new RequiredVariableNotPresentException();
        }
    }


}