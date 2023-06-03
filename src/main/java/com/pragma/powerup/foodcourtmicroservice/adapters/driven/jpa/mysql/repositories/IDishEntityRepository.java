package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IDishEntityRepository extends JpaRepository<DishEntity, Long> {
    @Query("SELECT d FROM DishEntity d JOIN d.idCategory c JOIN d.idRestaurant r WHERE r.id = :idRestaurant AND (c.id = :idCategory OR :idCategory IS NULL)")
    Page<DishEntity> getDishesByRestaurantAndCategory(Long idRestaurant, Long idCategory, Pageable pageable);
}
