package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.configuration.Constants;
import com.pragma.powerup.foodcourtmicroservice.domain.adapter.ExternalCommunicationDomainAdapter;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantOrderCommonServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.*;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.ArgumentValidations;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.PaginationValidations;

import java.util.List;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.*;

public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistancePort;
    private final ExternalCommunicationDomainAdapter externalCommunicationDomainAdapter;
    private final ITokenValidationPort tokenValidationPort;

    private final IRestaurantOrderCommonServicePort restaurantOrderCommonServicePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistancePort, ExternalCommunicationDomainAdapter externalCommunicationDomainAdapter, ITokenValidationPort tokenValidationPort,IRestaurantOrderCommonServicePort restaurantOrderCommonServicePort) {
        this.restaurantPersistancePort = restaurantPersistancePort;
        this.externalCommunicationDomainAdapter = externalCommunicationDomainAdapter;
        this.tokenValidationPort = tokenValidationPort;
        this.restaurantOrderCommonServicePort = restaurantOrderCommonServicePort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
            if (!restaurant.getName().matches(Constants.ALPHANUMERIC_BUT_NOT_ONLY_NUMBERS_REGEX))
                throw new NotValidNameRestaurantException();
            if (!restaurant.getPhone().matches(Constants.PHONE_REGEX))
                throw new InvalidPhoneException();
            if (!restaurant.getNit().matches(Constants.ONLY_NUMBERS_REGEX))
                throw new NotOnlyNumbersException();
            Boolean userHasRole = externalCommunicationDomainAdapter.userHasRole(restaurant.getIdOwner(), Constants.OWNER_ROLE_ID);
            if(userHasRole.equals(false))
                throw new UserHasNoPermissionException("The user provided in the request is not an owner");
            restaurantPersistancePort.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant findById(Long id) {
        return restaurantOrderCommonServicePort.findRestaurantById(id);
    }

    @Override
    public Boolean isTheRestaurantOwner(Long idUser, Restaurant restaurant) {
        return restaurant.getIdOwner().equals(idUser);
    }

    @Override
    public Boolean isTheRestaurantOwner(String token, Long idRestaurant) {
        ArgumentValidations.validateObject(idRestaurant, ID_RESTAURANT_STRING_VALUE);
        ArgumentValidations.validateString(token,TOKEN_MESSAGE);
        Long idUserFromToken = tokenValidationPort.findIdUserFromToken(token);
        Restaurant restaurant = findById(idRestaurant);
        //it isn't necessary check if the restaurant has an owner because if not, the domain model will throw a FailValidatingRequiredVariableException.
        return restaurant.getIdOwner().equals(idUserFromToken);
    }

    @Override
    public Boolean isTheRestaurantOwner(String token, Restaurant restaurant) {
        ArgumentValidations.validateString(token,TOKEN_MESSAGE);
        ArgumentValidations.validateObject(restaurant,"Restaurant");
        Long idUserFromToken = tokenValidationPort.findIdUserFromToken(token);
        return restaurant.getIdOwner().equals(idUserFromToken);
    }

    @Override
    public List<Restaurant> findAllPaged(Integer page, Integer sizePage, String token) {
        tokenValidationPort.verifyRoleInToken(token,CLIENT_ROLE_NAME);
        PaginationValidations.validatePageAndSizePage(page,sizePage);
        List<Restaurant> listRestaurants = restaurantPersistancePort.findAllPaged(page, sizePage, "name", ASC_DIRECTION_VALUE);
        if(listRestaurants.isEmpty())
            throw new NoDataFoundException("No restaurants found");
        return listRestaurants;
    }

    @Override
    public List<OrderDurationInfoDto> getDurationOfOrdersByRestaurant(Long idRestaurant, Integer page, Integer sizePage, String token) {
        PaginationValidations.validatePageAndSizePage(page,sizePage);
        Restaurant restaurant = findById(idRestaurant);
        if(Boolean.FALSE.equals(isTheRestaurantOwner(token, restaurant))){
            throw new UserHasNoPermissionException(USER_IS_NOT_THE_RESTAURANT_OWNER_MESSAGE);
        }
        return restaurantOrderCommonServicePort.getDurationOfFinalizedOrdersByRestaurant(restaurant, page, sizePage);
    }


}
