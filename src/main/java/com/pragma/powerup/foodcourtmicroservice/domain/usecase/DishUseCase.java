package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.ICategoryServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndAmountDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndRestaurantOwnerIdDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.EditDishInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.IdDishAndAmountDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDishFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.UserHasNoPermissionException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IDishPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.ArgumentValidations;
import com.pragma.powerup.foodcourtmicroservice.domain.validations.PaginationValidations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pragma.powerup.foodcourtmicroservice.domain.constants.DomainConstants.*;


public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final ICategoryServicePort categoryServicePort;
    private final IRestaurantServicePort restaurantServicePort;

    private final ITokenValidationPort tokenValidationPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, ICategoryServicePort categoryServicePort, IRestaurantServicePort restaurantServicePort, ITokenValidationPort tokenValidationPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.categoryServicePort = categoryServicePort;
        this.restaurantServicePort = restaurantServicePort;
        this.tokenValidationPort = tokenValidationPort;
    }

    @Override
    public Dish saveDish(DishAndRestaurantOwnerIdDto dishAndRestaurantOwnerIdDto, String token) {
        tokenValidationPort.verifyRoleInToken(token,OWNER_ROLE_NAME);
        //verify if the idOwnerRestaurant from body request is the same of the token provided.
        Boolean userIsInToken = tokenValidationPort.userIsInToken(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(), token);
        if (userIsInToken.equals(false))
            throw new UserHasNoPermissionException(USER_PROVIDED_DOES_MATCH_WITH_USER_TOKEN_MESSAGE);
        Restaurant restaurant = restaurantServicePort.findById(dishAndRestaurantOwnerIdDto.getDish().getRestaurant().getId());
        Category category = categoryServicePort.findById(dishAndRestaurantOwnerIdDto.getDish().getCategory().getId());
        //verify if the idOwnerRestaurant from body request is owner of the restaurant.
        if(Boolean.FALSE.equals(restaurantServicePort.isTheRestaurantOwner(dishAndRestaurantOwnerIdDto.getIdOwnerRestaurant(), restaurant))){
            throw new UserHasNoPermissionException(USER_IS_NOT_THE_RESTAURANT_OWNER_MESSAGE);
        }
        dishAndRestaurantOwnerIdDto.getDish().setCategory(category);
        dishAndRestaurantOwnerIdDto.getDish().setRestaurant(restaurant);
        return dishPersistencePort.saveDish(dishAndRestaurantOwnerIdDto.getDish());
    }

    @Override
    public Dish editDish(EditDishInfoDto editDishInfoDto, String token) {
        tokenValidationPort.verifyRoleInToken(token,OWNER_ROLE_NAME);
        //verify if the idOwnerRestaurant from body request is the same of the token provided.
        Boolean userIsInToken = tokenValidationPort.userIsInToken(editDishInfoDto.getIdOwnerRestaurant(), token);
        if (userIsInToken.equals(false))
            throw new UserHasNoPermissionException(USER_PROVIDED_DOES_MATCH_WITH_USER_TOKEN_MESSAGE);
        Dish dish = findById(editDishInfoDto.getIdDish());
        //verify if the idOwnerRestaurant from body request is owner of the restaurant.
        if(Boolean.FALSE.equals(restaurantServicePort.isTheRestaurantOwner(editDishInfoDto.getIdOwnerRestaurant(), dish.getRestaurant()))){
            throw new UserHasNoPermissionException(USER_IS_NOT_THE_RESTAURANT_OWNER_MESSAGE);
        }
        dish.setPrice(editDishInfoDto.getPrice());
        dish.setDescription(editDishInfoDto.getDescription());
        dishPersistencePort.saveDish(dish);
        return dish;
    }

    @Override
    public Dish findById(Long id) {
        ArgumentValidations.validateObject(id,"Id of dish");
        Dish dish = dishPersistencePort.findById(id);
        if (dish == null)
            throw new NoDishFoundException();
        return dish;
    }

    @Override
    public Dish changeStatusDish(Long idDish, Boolean status, String token) {
        tokenValidationPort.verifyRoleInToken(token,OWNER_ROLE_NAME);
        Dish dish = findById(idDish);
        if(Boolean.FALSE.equals(restaurantServicePort.isTheRestaurantOwner(token,dish.getRestaurant()))){
            throw new UserHasNoPermissionException(USER_IS_NOT_THE_RESTAURANT_OWNER_MESSAGE);
        }
        dish.setActive(status);
        return dishPersistencePort.saveDish(dish);
    }

    @Override
    public List<Dish> getPagedDishesByRestaurantAndOptionalCategory(Long idRestaurant, Integer page, Integer sizePage, Optional<Long> idCategory, String token) {
        tokenValidationPort.verifyRoleInToken(token,CLIENT_ROLE_NAME);
        PaginationValidations.validatePageAndSizePage(page,sizePage);
        Restaurant restaurant = restaurantServicePort.findById(idRestaurant);
        Category category = null;
        if (idCategory.isPresent())
             category = categoryServicePort.findById(idCategory.get());
        List<Dish> dishesByRestaurantAndCategory = dishPersistencePort.getDishesByRestaurantAndCategory(page, sizePage, restaurant.getId(),
                category != null ? category.getId() : null);
        if(dishesByRestaurantAndCategory.isEmpty())
            throw new NoDataFoundException("No dishes found");
        return dishesByRestaurantAndCategory;
    }

    @Override
    public List<DishAndAmountDto> generateValidatedDishList(Long idRestaurant, List<IdDishAndAmountDto> infoDishes) {
        if(infoDishes.isEmpty())
            throw new NoDataFoundException("No dishes provided in the request");
        List<DishAndAmountDto> dishAndAmountDtoList = new ArrayList<>();
        try{
            infoDishes.stream()
                    .collect(Collectors.groupingBy(IdDishAndAmountDto::getIdDish, Collectors.summingInt(IdDishAndAmountDto::getAmount))) // sum the amount of repeated dishes.
                    .entrySet().stream() //the result is a Set so now the key is the idDish, the value is the amount.
                    .forEach(infoDish -> {
                        Long idDish = infoDish.getKey();
                        Integer amount = infoDish.getValue();
                        Dish dish = findById(idDish);
                        if (dish.getActive().equals(false) || !dish.getRestaurant().getId().equals(idRestaurant))
                            throw new FailValidatingRequiredVariableException("Dish with id: "+idDish+ ", is disabled or is not from the restaurant with id: " + idRestaurant);
                        if (amount <= 0)
                            throw new FailValidatingRequiredVariableException("Amount is not valid for dish: " + idDish);
                        dishAndAmountDtoList.add(new DishAndAmountDto(dish,amount));
                    });
            return dishAndAmountDtoList;
        } catch (NullPointerException e){
            throw new FailValidatingRequiredVariableException("List of dishes is malformed");
        }
    }
}
