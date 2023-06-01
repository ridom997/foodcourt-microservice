package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.configuration.Constants;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.*;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserValidationComunicationPort;

import java.util.List;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.*;

public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistancePort;
    private final IUserValidationComunicationPort userValidationComunicationPort;
    private final ITokenValidationPort tokenValidationPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistancePort, IUserValidationComunicationPort userValidationComunicationPort, ITokenValidationPort tokenValidationPort) {
        this.restaurantPersistancePort = restaurantPersistancePort;
        this.userValidationComunicationPort = userValidationComunicationPort;
        this.tokenValidationPort = tokenValidationPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
            if (!restaurant.getName().matches(Constants.ALPHANUMERIC_BUT_NOT_ONLY_NUMBERS_REGEX))
                throw new NotValidNameRestaurantException();
            if (!restaurant.getPhone().matches(Constants.PHONE_REGEX))
                throw new InvalidPhoneException();
            if (!restaurant.getNit().matches(Constants.ONLY_NUMBERS_REGEX))
                throw new NotOnlyNumbersException();
            Boolean userHasRole = userValidationComunicationPort.userHasRole(restaurant.getIdOwner(), Constants.OWNER_ROLE_ID);
            if(userHasRole.equals(false))
                throw new UserHasNoPermissionException("The user provided in the request is no an owner");
            restaurantPersistancePort.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant findById(Long id) {
        Restaurant restaurant = restaurantPersistancePort.findById(id);
        if (restaurant == null)
            throw new NoRestaurantFoundException();
        return restaurant;
    }

    @Override
    public Boolean isTheRestaurantOwner(Long idUser, Restaurant restaurant) {
        return restaurant.getIdOwner().equals(idUser);
    }

    @Override
    public Boolean isTheRestaurantOwner(String token, Long idRestaurant) {
        if(idRestaurant == null)
            throw new FailValidatingRequiredVariableException("idRestaurant is not present");
        if(token == null)
            throw new FailValidatingRequiredVariableException("Token is not present");
        Long idUserFromToken = tokenValidationPort.findIdUserFromToken(token);
        Restaurant restaurant = findById(idRestaurant);
        //it isn't necessary check if the restaurant has an owner because if not, the domain model will throw a FailValidatingRequiredVariableException.
        return restaurant.getIdOwner().equals(idUserFromToken);
    }

    @Override
    public Boolean isTheRestaurantOwner(String token, Restaurant restaurant) {
        if(token ==null)
            throw new FailValidatingRequiredVariableException("Token is not present");
        if(restaurant == null)
            throw new FailValidatingRequiredVariableException("Restaurant is not present");
        Long idUserFromToken = tokenValidationPort.findIdUserFromToken(token);
        return restaurant.getIdOwner().equals(idUserFromToken);
    }

    @Override
    public List<Restaurant> findAllPaged(Integer page, Integer sizePage, String token) {
        tokenValidationPort.verifyRoleInToken(token,CLIENT_ROLE_NAME);
        if(page == null || page < 0)
            throw new FailValidatingRequiredVariableException(PAGE_NOT_VALID_MESSAGE);
        if(sizePage == null || sizePage <= 0)
            throw new FailValidatingRequiredVariableException(SIZE_PAGE_NOT_VALID_MESSAGE);
        List<Restaurant> listRestaurants = restaurantPersistancePort.findAllPaged(page, sizePage, "name", ASC_DIRECTION_VALUE);
        if(listRestaurants.isEmpty())
            throw new NoDataFoundException("No restaurants found");
        return listRestaurants;
    }


}
