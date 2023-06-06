package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;

public class DishAndAmountDto {

    private Dish dish;
    private Integer amount;

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public DishAndAmountDto(Dish dish, Integer amount) {
        this.dish = dish;
        this.amount = amount;
    }

    public DishAndAmountDto() {
    }
}
