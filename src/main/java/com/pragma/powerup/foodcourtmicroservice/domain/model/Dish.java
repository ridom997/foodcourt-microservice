package com.pragma.powerup.foodcourtmicroservice.domain.model;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;

public class Dish {
    private Long id;
    private String name;
    private Category category;
    private String description;
    private Integer price;
    private Restaurant restaurant;
    private String urlImage;
    private Boolean active = true;


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
        if (name == null || name.isEmpty()) throw new FailValidatingRequiredVariableException("Name is not present");
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null ) throw new FailValidatingRequiredVariableException("Category is not present");
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) throw new FailValidatingRequiredVariableException("Description is not present");
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        if (price == null || price <= 0) throw new FailValidatingRequiredVariableException("Price is not present or not valid");
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        if (restaurant == null) throw new FailValidatingRequiredVariableException("Restaurant is not present");
        this.restaurant = restaurant;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        if (urlImage == null || urlImage.isEmpty()) throw new FailValidatingRequiredVariableException("Url of image is not present");
        this.urlImage = urlImage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Dish() {
    }

    public Dish(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", restaurant=" + restaurant +
                ", urlImage='" + urlImage + '\'' +
                ", active=" + active +
                '}';
    }
}
