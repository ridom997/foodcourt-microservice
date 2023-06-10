package com.pragma.powerup.foodcourtmicroservice.configuration;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.adapter.TraceabilityMicroFeignAdapter;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.adapter.UserValidationFeignAdapter;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter.*;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.*;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.*;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.adapter.TokenValidationSpringAdapter;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.jwt.JwtProvider;
import com.pragma.powerup.foodcourtmicroservice.domain.api.*;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.*;
import com.pragma.powerup.foodcourtmicroservice.domain.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantEntityRepository restaurantEntityRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    private final UserValidationFeignAdapter userValidationServicePort;
    private final TraceabilityMicroFeignAdapter traceabilityCommunicationPort;

    private final IDishEntityMapper iDishEntityMapper;
    private final IDishEntityRepository dishEntityRepository;

    private final ICategoryEntityMapper categoryEntityMapper;
    private final ICategoryEntityRepository categoryEntityRepository;

    private final JwtProvider jwtProvider;

    private final IOrderDishEntityMapper orderDishEntityMapper;
    private final IOrderDishEntityRepository orderDishEntityRepository;

    private final IOrderEntityMapper orderEntityMapper;
    private final IOrderEntityRepository orderEntityRepository;


    @Bean
    public IRestaurantPersistencePort restaurantPersistancePort(){
        return new RestaurantMysqlAdapter(restaurantEntityRepository,restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(){
        return new RestaurantUseCase(restaurantPersistancePort(), userValidationServicePort, tokenValidationPort());
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
    public ITokenValidationPort tokenValidationPort(){
        return new TokenValidationSpringAdapter(jwtProvider);
    }
    @Bean
    public IDishServicePort dishServicePort(){
        return new DishUseCase(dishPersistancePort(),categoryServicePort(),restaurantServicePort(), tokenValidationPort());
    }

    @Bean
    public IOrderDishPersistencePort orderDishPersistencePort(){
        return new OrderDishMysqlAdapter(orderDishEntityRepository,orderDishEntityMapper,orderEntityMapper);
    }
    @Bean
    public IOrderPersistencePort orderPersistencePort(){
        return new OrderMysqlAdapter(orderEntityRepository,orderEntityMapper, traceabilityCommunicationPort);
    }

    @Bean
    public IOrderDishServicePort orderDishServicePort(){
        return new OrderDishUseCase(orderDishPersistencePort());
    }
    @Bean
    public IOrderServicePort orderServicePort(){
        return new OrderUseCase(orderPersistencePort(),
                tokenValidationPort(),
                dishServicePort(),
                restaurantServicePort(),
                orderDishServicePort(),userValidationServicePort);
    }
}
