package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;

public class DishAndRestaurantOwnerIdDto {
    private Dish dish;
    private Long idOwnerRestaurant;

    public DishAndRestaurantOwnerIdDto(Dish dish, Long idOwnerRestaurant) {
        setDish(dish);
        setIdOwnerRestaurant(idOwnerRestaurant);
    }
    public DishAndRestaurantOwnerIdDto() {
    }
    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        if (dish == null) throw new FailValidatingRequiredVariableException("Dish is not present");
        this.dish = dish;
    }

    public Long getIdOwnerRestaurant() {
        return idOwnerRestaurant;
    }

    public void setIdOwnerRestaurant(Long idOwnerRestaurant) {
        if (idOwnerRestaurant == null) throw new FailValidatingRequiredVariableException("idOwnerRestaurant is not present");
        this.idOwnerRestaurant = idOwnerRestaurant;
    }
}
