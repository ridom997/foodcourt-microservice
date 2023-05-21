package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;

public class EditDishInfoDto {
    private Long idOwnerRestaurant;
    private Long idDish;
    private Integer price;
    private String description;

    public Long getIdOwnerRestaurant() {
        return idOwnerRestaurant;
    }

    public void setIdOwnerRestaurant(Long idOwnerRestaurant) {
        if (idOwnerRestaurant == null ) throw new FailValidatingRequiredVariableException("Id Owner Restaurant is not present");
        this.idOwnerRestaurant = idOwnerRestaurant;
    }

    public Long getIdDish() {
        return idDish;
    }

    public void setIdDish(Long idDish) {
        if (idDish == null ) throw new FailValidatingRequiredVariableException("idDish is not present");
        this.idDish = idDish;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        if (price == null || price <= 0) throw new FailValidatingRequiredVariableException("Price is not present or not valid");
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) throw new FailValidatingRequiredVariableException("Description is not present");
        this.description = description;
    }

    public EditDishInfoDto() {
    }

    public EditDishInfoDto(Long idOwnerRestaurant, Long idDish, Integer price, String description) {
        setIdOwnerRestaurant(idOwnerRestaurant);
        setIdDish(idDish);
        setPrice(price);
        setDescription(description);
    }
}
