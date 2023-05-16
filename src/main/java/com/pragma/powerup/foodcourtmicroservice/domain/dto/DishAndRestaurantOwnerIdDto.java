package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.RequiredVariableNotPresentException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;

public class DishAndRestaurantOwnerIdDto {
    private Dish dish;
    private Long idOwnerRestaurant;

    public DishAndRestaurantOwnerIdDto(Dish dish, Long idOwnerRestaurant) {
        this.dish = dish;
        if (idOwnerRestaurant == null) throw new RequiredVariableNotPresentException();
        this.idOwnerRestaurant = idOwnerRestaurant;
    }
    public DishAndRestaurantOwnerIdDto() {
    }
    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Long getIdOwnerRestaurant() {
        return idOwnerRestaurant;
    }

    public void setIdOwnerRestaurant(Long idOwnerRestaurant) {
        this.idOwnerRestaurant = idOwnerRestaurant;
    }
}
