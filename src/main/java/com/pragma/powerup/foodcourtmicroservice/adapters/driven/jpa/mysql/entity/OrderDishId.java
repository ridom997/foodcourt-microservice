package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class OrderDishId implements Serializable {
    private static final long serialVersionUID = -5172588358390562429L;
    @NotNull
    @Column(name = "id_order", nullable = false)
    private Long idOrder;

    @NotNull
    @Column(name = "id_dish", nullable = false)
    private Long idDish;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderDishId entity = (OrderDishId) o;
        return Objects.equals(this.idOrder, entity.idOrder) &&
                Objects.equals(this.idDish, entity.idDish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrder, idDish);
    }

}