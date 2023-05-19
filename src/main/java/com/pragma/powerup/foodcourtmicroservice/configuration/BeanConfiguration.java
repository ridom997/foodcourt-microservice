package com.pragma.powerup.foodcourtmicroservice.configuration;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.adapter.UserValidationFeignAdapter;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter.CategoryMysqlAdapter;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter.DishMysqlAdapter;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter.RestaurantMysqlAdapter;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.ICategoryEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.ICategoryEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IDishEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.api.ICategoryServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IDishPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.foodcourtmicroservice.domain.usecase.CategoryUseCase;
import com.pragma.powerup.foodcourtmicroservice.domain.usecase.DishUseCase;
import com.pragma.powerup.foodcourtmicroservice.domain.usecase.RestaurantUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantEntityRepository restaurantEntityRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    private final UserValidationFeignAdapter userValidationServicePort;

    private final IDishEntityMapper iDishEntityMapper;
    private final IDishEntityRepository dishEntityRepository;

    private final ICategoryEntityMapper categoryEntityMapper;
    private final ICategoryEntityRepository categoryEntityRepository;

    @Bean
    public IRestaurantPersistencePort restaurantPersistancePort(){
        return new RestaurantMysqlAdapter(restaurantEntityRepository,restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(){
        return new RestaurantUseCase(restaurantPersistancePort(), userValidationServicePort);
    }

    @Bean
    public IDishPersistencePort dishPersistancePort(){
        return new DishMysqlAdapter(dishEntityRepository,iDishEntityMapper);
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort(){
        return new CategoryMysqlAdapter(categoryEntityRepository,categoryEntityMapper);
    }

    @Bean
    public ICategoryServicePort categoryServicePort(){
        return new CategoryUseCase(categoryPersistencePort());
    }

    @Bean
    public IDishServicePort dishServicePort(){
        return new DishUseCase(dishPersistancePort(),categoryServicePort(),restaurantServicePort());
    }
}
