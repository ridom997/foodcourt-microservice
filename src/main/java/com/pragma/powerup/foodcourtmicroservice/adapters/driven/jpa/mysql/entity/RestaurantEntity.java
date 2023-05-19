package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "restaurant")
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @NotNull
    @Column(name = "id_owner", nullable = false)
    private Long idOwner;

    @Size(max = 13)
    @Column(name = "phone", length = 13)
    private String phone;

    @Size(max = 100)
    @Column(name = "url_logo", length = 100)
    private String urlLogo;

    @Size(max = 100)
    @Column(name = "nit", length = 100)
    private String nit;

    @OneToMany(mappedBy = "idRestaurant")
    private Set<DishEntity> dishEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idRestaurant")
    private Set<OrderEntity> orderEntitySet = new LinkedHashSet<>();

    public RestaurantEntity(Long id) {
        this.id = id;
    }
}