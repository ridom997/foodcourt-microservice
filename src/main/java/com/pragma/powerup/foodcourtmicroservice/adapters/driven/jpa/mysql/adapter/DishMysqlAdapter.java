package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IDishEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IDishPersistencePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DishMysqlAdapter implements IDishPersistencePort {

    private final IDishEntityRepository dishEntityRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public void saveDish(Dish dish) {
        dishEntityRepository.save(dishEntityMapper.toEntity(dish));
    }

    @Override
    public Dish findById(Long id) {
        return null;
    }
}
