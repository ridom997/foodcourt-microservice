package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.ICategoryEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.ICategoryEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ICategoryPersistencePort;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class CategoryMysqlAdapter implements ICategoryPersistencePort {

    private final ICategoryEntityRepository categoryEntityRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Category findById(Long id) {
        Optional<CategoryEntity> optionalCategoryEntity = categoryEntityRepository.findById(id);
        if (optionalCategoryEntity.isPresent()) return categoryEntityMapper.toCategory(optionalCategoryEntity.get());
        return null;
    }
}
