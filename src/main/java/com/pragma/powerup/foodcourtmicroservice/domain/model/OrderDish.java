package com.pragma.powerup.foodcourtmicroservice.domain.model;

public class OrderDish {

    private Order order;
    private Dish dish;
    private Integer amount;

    public OrderDish() {
    }

    public OrderDish(Order order, Dish dish, Integer amount) {
        this.order = order;
        this.dish = dish;
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

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
}
