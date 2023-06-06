package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import java.util.List;

public class NewOrderDto {
    private Long idRestaurant;
    private List<IdDishAndAmountDto> dishes;

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public List<IdDishAndAmountDto> getDishes() {
        return dishes;
    }

    public void setDishes(List<IdDishAndAmountDto> dishes) {
        this.dishes = dishes;
    }

    public NewOrderDto() {
    }

    public NewOrderDto(Long idRestaurant, List<IdDishAndAmountDto> dishes) {
        this.idRestaurant = idRestaurant;
        this.dishes = dishes;
    }
}
