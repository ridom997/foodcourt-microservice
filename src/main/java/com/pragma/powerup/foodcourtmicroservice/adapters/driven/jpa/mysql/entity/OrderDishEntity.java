package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_dish")
public class OrderDishEntity {
    @EmbeddedId
    private OrderDishId id;

    @MapsId("idOrderEntity")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_order", nullable = false)
    private OrderEntity idOrder;

    @MapsId("idDishEntity")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dish", nullable = false)
    private DishEntity idDish;

    @Column(name = "amount")
    private Integer amount;

}