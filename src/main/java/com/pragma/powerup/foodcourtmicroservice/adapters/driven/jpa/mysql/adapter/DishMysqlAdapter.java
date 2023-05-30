package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IDishEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Dish;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IDishPersistencePort;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class DishMysqlAdapter implements IDishPersistencePort {

    private final IDishEntityRepository dishEntityRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public Dish saveDish(Dish dish) {
        DishEntity dishEntity = dishEntityRepository.save(dishEntityMapper.toEntity(dish));
        dish.setId(dishEntity.getId());
        return dish;
    }

    @Override
    public Dish findById(Long id) {
        Optional<DishEntity> dishEntityOptional = dishEntityRepository.findById(id);
        if (dishEntityOptional.isEmpty())
            return null;
        return dishEntityMapper.toDish(dishEntityOptional.get());
    }
}
