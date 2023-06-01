package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class RestaurantMysqlAdapter implements IRestaurantPersistencePort {

    private final IRestaurantEntityRepository restaurantEntityRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantEntityRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }

    @Override
    public Restaurant findById(Long id) {
        Optional<RestaurantEntity> restaurantEntityOptional = restaurantEntityRepository.findById(id);
        if (restaurantEntityOptional.isPresent()) return restaurantEntityMapper.toRestaurant(restaurantEntityOptional.get());
        return null;
    }

    @Override
    public List<Restaurant> findAllPaged(Integer page, Integer sizePage, String sortAttribute, String direction) {
        Sort sortObject = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortAttribute).ascending()
                : Sort.by(sortAttribute).descending();

        Pageable pageable = PageRequest.of(page, sizePage, sortObject);
        Page<RestaurantEntity> restaurantEntities = restaurantEntityRepository.findAll(pageable);
        return restaurantEntities.stream()
                .map(restaurantEntityMapper::toRestaurant)
                .toList();
    }
}
