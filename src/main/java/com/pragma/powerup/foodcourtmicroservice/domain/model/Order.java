package com.pragma.powerup.foodcourtmicroservice.domain.model;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long idClient;
    private LocalDateTime date;
    private Integer status;
    private Long idChef;
    private Restaurant restaurant;

    private LocalDateTime dateFinished;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getIdChef() {
        return idChef;
    }

    public void setIdChef(Long idChef) {
        this.idChef = idChef;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDateTime getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(LocalDateTime dateFinished) {
        this.dateFinished = dateFinished;
    }

    public Order() {
    }

    public Order(Long idClient, LocalDateTime date, Integer status, Long idChef, Restaurant restaurant) {
        this.idClient = idClient;
        this.date = date;
        this.status = status;
        this.idChef = idChef;
        this.restaurant = restaurant;
    }
}
