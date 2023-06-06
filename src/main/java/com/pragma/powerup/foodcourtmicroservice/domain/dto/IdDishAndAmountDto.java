package com.pragma.powerup.foodcourtmicroservice.domain.dto;

public class IdDishAndAmountDto {
    private Long idDish;
    private Integer amount;

    public Long getIdDish() {
        return idDish;
    }

    public void setIdDish(Long idDish) {
        this.idDish = idDish;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public IdDishAndAmountDto(Long idDish, Integer amount) {
        this.idDish = idDish;
        this.amount = amount;
    }

    public IdDishAndAmountDto() {
    }
}
