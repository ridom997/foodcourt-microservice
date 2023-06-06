package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dish")
public class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_category", nullable = false)
    private CategoryEntity idCategory;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "price")
    private Integer price;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_restaurant", nullable = false)
    private RestaurantEntity idRestaurant;

    @Size(max = 100)
    @Column(name = "url_image", length = 100)
    private String urlImage;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "idDish")
    private Set<OrderDishEntity> orderDishEntities = new LinkedHashSet<>();

    public DishEntity() {
    }

    public DishEntity(Long id) {
        this.id = id;
    }
}