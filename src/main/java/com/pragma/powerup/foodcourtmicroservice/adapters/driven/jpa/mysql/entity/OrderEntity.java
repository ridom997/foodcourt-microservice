package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "`order`")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "id_client", nullable = false, length = 100)
    private Long idClient;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "status")
    private Integer status;

    @NotNull
    @Column(name = "id_chef", nullable = false)
    private Long idChef;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_restaurant", nullable = false)
    private RestaurantEntity idRestaurant;

    @OneToMany(mappedBy = "idOrder")
    private Set<OrderDishEntity> orderDishEntities = new LinkedHashSet<>();

    public OrderEntity() {
    }

    public OrderEntity(Long id) {
        this.id = id;
    }
}