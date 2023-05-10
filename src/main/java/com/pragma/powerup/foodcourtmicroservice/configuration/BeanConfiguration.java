package com.pragma.powerup.foodcourtmicroservice.configuration;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.adapter.UserValidationFeignAdapter;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter.RestaurantMysqlAdapter;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistancePort;
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

    @Bean
    public IRestaurantPersistancePort restaurantPersistancePort(){
        return new RestaurantMysqlAdapter(restaurantEntityRepository,restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(){
        return new RestaurantUseCase(restaurantPersistancePort(), userValidationServicePort);
    }
}
