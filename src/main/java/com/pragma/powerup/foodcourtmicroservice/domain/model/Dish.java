package com.pragma.powerup.foodcourtmicroservice.domain.model;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.RequiredVariableNotPresentException;

public class Dish {
    private Long id;
    private String name;
    private Category category;
    private String description;
    private Integer price;
    private Restaurant restaurant;
    private String urlImage;
    private Boolean active = true;

    public Dish(Long id, String name, Category category, String description, Integer price, Restaurant restaurant, String urlImage) {
        if (category == null ) throw new RequiredVariableNotPresentException();
        if (restaurant == null) throw new RequiredVariableNotPresentException();
        if (name == null || name.isEmpty()) throw new RequiredVariableNotPresentException();
        if (description == null || description.isEmpty()) throw new RequiredVariableNotPresentException();
        if (price == null || price <= 0) throw new RequiredVariableNotPresentException();
        if (urlImage == null || urlImage.isEmpty()) throw new RequiredVariableNotPresentException();
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
        this.urlImage = urlImage;
    }

    public Dish() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
